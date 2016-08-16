package com.ai.api.service.impl;

import com.ai.api.dao.CustomerDao;
import com.ai.api.dao.PaymentDao;
import com.ai.api.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Date;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.service.impl
 * Creation Date   : 2016/8/12 17:36
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    @Autowired
    @Qualifier("customerDao")
    private CustomerDao customerDao;

    @Override
    public boolean downloadProformaInvoicePDF(String userId,String invoiceId,HttpServletResponse httpResponse){
        boolean b = false;
        try {
            String login = customerDao.getGeneralUser(userId).getLogin();
            httpResponse.setHeader("Content-Disposition", "attachment; filename=attachment-"+new Date().getTime()+".pdf");
            InputStream inputStream =  paymentDao.downloadProformaInvoicePDF(login,invoiceId);
            ServletOutputStream output = httpResponse.getOutputStream();
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            if (null!=inputStream) {
                byte[] buffer = new byte[10240];
                for (int length = 0; (length = inputStream.read(buffer)) > 0; ) {
                    output.write(buffer, 0, length);
                }
            }
            b = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return b;
    }
}
