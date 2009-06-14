/*
*  Licensed to the Apache Software Foundation (ASF) under one
*  or more contributor license agreements.  See the NOTICE file
*  distributed with this work for additional information
*  regarding copyright ownership.  The ASF licenses this file
*  to you under the Apache License, Version 2.0 (the
*  "License"); you may not use this file except in compliance
*  with the License.  You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/
package org.apache.synapse.transport.vfs;

import java.text.DateFormat;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.transport.base.AbstractPollTableEntry;

/**
 * Holds information about an entry in the VFS transport poll table used by the
 * VFS Transport Listener
 */
public class PollTableEntry extends AbstractPollTableEntry {

    // operation after scan
    public static final int DELETE = 0;
    public static final int MOVE   = 1;

    /** File or Directory to scan */
    private String fileURI;
    /** file name pattern for a directory or compressed file entry */
    private String fileNamePattern;
    /** Content-Type to use for the message */
    private String contentType;

    /** action to take after a successful poll */
    private int actionAfterProcess = DELETE;
    /** action to take after a poll with errors */
    private int actionAfterErrors = DELETE;
    /** action to take after a failed poll */
    private int actionAfterFailure = DELETE;

    /** where to move the file after processing */
    private String moveAfterProcess;
    /** where to move the file after encountering some errors */
    private String moveAfterErrors;
    /** where to move the file after total failure */
    private String moveAfterFailure;
    /** moved file will have this formatted timestamp prefix */    
    private DateFormat moveTimestampFormat;

    private boolean streaming;

    private int maxRetryCount;
    private long reconnectTimeout;

    @Override
    public EndpointReference[] getEndpointReferences(String ip) {
        return new EndpointReference[] { new EndpointReference("vfs:" + fileURI) };
    }

    public String getFileURI() {
        return fileURI;
    }

    public void setFileURI(String fileURI) {
        if (fileURI.startsWith(VFSConstants.VFS_PREFIX)) {
            this.fileURI = fileURI.substring(VFSConstants.VFS_PREFIX.length());
        } else {
            this.fileURI = fileURI;
        }
    }

    public String getFileNamePattern() {
        return fileNamePattern;
    }

    public void setFileNamePattern(String fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getActionAfterProcess() {
        return actionAfterProcess;
    }

    public void setActionAfterProcess(int actionAfterProcess) {
        this.actionAfterProcess = actionAfterProcess;
    }

    public int getActionAfterErrors() {
        return actionAfterErrors;
    }

    public void setActionAfterErrors(int actionAfterErrors) {
        this.actionAfterErrors = actionAfterErrors;
    }

    public int getActionAfterFailure() {
        return actionAfterFailure;
    }

    public void setActionAfterFailure(int actionAfterFailure) {
        this.actionAfterFailure = actionAfterFailure;
    }

    public String getMoveAfterProcess() {
        return moveAfterProcess;
    }

    public void setMoveAfterProcess(String moveAfterProcess) {
        if (moveAfterProcess == null) {
            this.moveAfterProcess = null;
        } else if (moveAfterProcess.startsWith(VFSConstants.VFS_PREFIX)) {
            // to recover a good directory location if user entered with the vfs: prefix
            // because transport uris are given like that
            this.moveAfterProcess = moveAfterProcess.substring(VFSConstants.VFS_PREFIX.length());
        } else {
            this.moveAfterProcess = moveAfterProcess;
        }
    }

    public String getMoveAfterErrors() {
        return moveAfterErrors;
    }

    public void setMoveAfterErrors(String moveAfterErrors) {
        if (moveAfterErrors == null) {
            this.moveAfterErrors = null;
        } else if (moveAfterErrors.startsWith(VFSConstants.VFS_PREFIX)) {
            this.moveAfterErrors = moveAfterErrors.substring(VFSConstants.VFS_PREFIX.length());
        } else {
            this.moveAfterErrors = moveAfterErrors;
        }  
    }

    public String getMoveAfterFailure() {
        return moveAfterFailure;
    }

    public void setMoveAfterFailure(String moveAfterFailure) {
        if (moveAfterFailure == null) {
            this.moveAfterFailure = null;
        } else if (moveAfterFailure.startsWith(VFSConstants.VFS_PREFIX)) {
            this.moveAfterFailure = moveAfterFailure.substring(VFSConstants.VFS_PREFIX.length());
        } else {
            this.moveAfterFailure = moveAfterFailure;
        }
    }

    public boolean isStreaming() {
        return streaming;
    }

    public void setStreaming(boolean streaming) {
        this.streaming = streaming;
    }

    public int getMaxRetryCount() {
      return maxRetryCount;
    }

    public void setMaxRetryCount(int maxRetryCount) {
      this.maxRetryCount = maxRetryCount;
    }

    public long getReconnectTimeout() {
      return reconnectTimeout;
    }

    public void setReconnectTimeout(long reconnectTimeout) {
      this.reconnectTimeout = reconnectTimeout;
    }

    public DateFormat getMoveTimestampFormat() {
        return moveTimestampFormat;
    }

    public void setMoveTimestampFormat(DateFormat moveTimestampFormat) {
        this.moveTimestampFormat = moveTimestampFormat;
    }
}
