package com.tenga.notification.model.mapper;

import com.tenga.notification.model.dto.NotificationResponse;
import com.tenga.notification.model.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

  NotificationResponse toResponse(Notification notification);
}
