package com.ai.api.dao;

import com.ai.commons.beans.fileservice.FileMetaBean;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p>
 * Package Name    : com.ai.api.dao
 * <p>
 * Creation Date   : 2016/6/30 12:44
 * <p>
 * Author          : Jianxiong Cai
 * <p>
 * Purpose         : TODO
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/
public interface FileDao {

    FileMetaBean getFileDetailInfo(String fileId);

    byte[] downloadFile(String fileId);
}
