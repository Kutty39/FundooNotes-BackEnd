package com.blbz.fundoonotebackend.serviceimpl;

import com.blbz.fundoonotebackend.dto.MsgDto;
import com.blbz.fundoonotebackend.service.Subcriber;
import com.blbz.fundoonotebackend.utility.Util;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class SubcriberImpl implements Subcriber {
    @Autowired
    private Util util;
    @Override
    @RabbitListener(queues="${jsa.rabbitmq.queue}")
    public void getMessage(MsgDto msgDto) throws MessagingException {
        System.out.println("Received Message: " + msgDto);
        util.sendEmail(msgDto);
    }
}