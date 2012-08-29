/*
 * Copyright (c) 2001-2004 Ant-Contrib project.  All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 package net.skiy.pants.tasks.antserver.server;

import java.io.*;
import java.net.Socket;

import org.apache.tools.ant.Project;
import org.apache.xml.serialize.EncodingInfo;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;


import net.skiy.pants.tasks.antserver.Command;
import net.skiy.pants.tasks.antserver.Response;
import net.skiy.pants.tasks.antserver.Util;
import net.skiy.pants.tasks.antserver.commands.DisconnectCommand;
import net.skiy.pants.tasks.antserver.commands.ShutdownCommand;

/****************************************************************************
 * Place class description here.
 *
 * @author <a href='mailto:mattinger@yahoo.com'>Matthew Inger</a>
 * @author		<additional author>
 *
 * @since
 *
 ****************************************************************************/


public class ConnectionHandler
        implements Runnable
{
    private static long nextGroupId = 0;
    private ServerTask task;
    private Socket socket;
    private Thread thread;
    private Throwable thrown;

    public ConnectionHandler(ServerTask task, Socket socket)
    {
        super();
        this.socket = socket;
        this.task = task;
    }

    public void start()
    {
        long gid = nextGroupId;
        if (nextGroupId == Long.MAX_VALUE)
            nextGroupId = 0;
        else
            nextGroupId++;

        ThreadGroup group = new ThreadGroup("server-tg-" + gid);
        thread = new Thread(group, this);
        thread.start();
    }

    public Throwable getThrown()
    {
        return thrown;
    }

    public void run()
    {
        InputStream is = null;
        OutputStream os = null;


        try
        {
            ConnectionBuildListener cbl = null;

            is = socket.getInputStream();
            os = socket.getOutputStream();

            ObjectInputStream ois = new ObjectInputStream(is);
            ObjectOutputStream oos = new ObjectOutputStream(os);

            // Write the initial response object so that the
            // object stream is initialized
            oos.writeObject(new Response());
            System.out.println("wrote initial response");
            boolean disconnect = false;
            Command inputCommand = null;
            Response response = null;

            while (! disconnect)
            {
                task.getProject().log("Reading command object.",
                        Project.MSG_DEBUG);
                System.out.println("read command");
                inputCommand = (Command) ois.readObject();
                System.out.println("got command" + inputCommand);
                task.getProject().log("Executing command object: " + inputCommand,
                        Project.MSG_DEBUG);

                response = new Response();

                try
                {
                    cbl = new ConnectionBuildListener();
                    task.getProject().addBuildListener(cbl);
                    System.out.println("exec command");
                    inputCommand.execute(task.getProject(),
                            inputCommand.getContentLength(),
                            is);
                    System.out.println("command finished");
                    response.setSucceeded(true);
                }
                catch (Throwable t)
                {
                    t.printStackTrace();
                    response.setSucceeded(false);
                    response.setThrowable(t);
                }
                finally
                {
                    if (cbl != null)
                        task.getProject().removeBuildListener(cbl);
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                XMLSerializer serial = new XMLSerializer();
                OutputFormat fmt = new OutputFormat();
                fmt.setOmitDocumentType(true);
                fmt.setOmitXMLDeclaration(false);
                serial.setOutputFormat(fmt);
                serial.setOutputByteStream(baos);
                serial.serialize(cbl.getDocument());
                response.setResultsXml(baos.toString());

                task.getProject().log("Executed command object: " + inputCommand,
                        Project.MSG_DEBUG);

                task.getProject().log("Sending response: " + response,
                        Project.MSG_DEBUG);

                response.setContentLength(inputCommand.getContentLength());
                System.out.println("wirteing response" + response.getResultsXml());
                
                oos.writeObject(response);

                if (inputCommand.getResponseContentLength() != 0)
                {
                    Util.transferBytes(inputCommand.getReponseContentStream(),
                            inputCommand.getResponseContentLength(),
                            os,
                            true);
                }

                if (inputCommand instanceof DisconnectCommand)
                {
                	System.out.println("disconnect");
                    disconnect = true;
                    task.getProject().log("Got disconnect command",
                            Project.MSG_DEBUG);
                }
                else if (inputCommand instanceof ShutdownCommand)
                {
                    disconnect = true;
                    System.out.println("shutdown");
                    task.getProject().log("Got shutdown command",
                            Project.MSG_DEBUG);
                    task.shutdown();
                }

            }

        }
        catch (ClassNotFoundException e)
        {
        	e.printStackTrace();
            thrown = e;
        }
        catch (IOException e)
        {
        	e.printStackTrace();
            thrown = e;
        }
        catch (Throwable t)
        {
        	t.printStackTrace();
            thrown = t;
        }
        finally
        {
            if (is != null)
            {
                try
                {
                	System.out.println("input close");
                    is.close();
                }
                catch (IOException e)
                {

                }
            }

            if (os != null)
            {
                try
                {
                	System.out.println("output close");
                    os.close();
                }
                catch (IOException e)
                {

                }
            }

            if (socket != null)
            {
                try
                {
                	System.out.println("socket closed");
                    socket.close();
                }
                catch (IOException e)
                {

                }
            }

        }
    }
}
