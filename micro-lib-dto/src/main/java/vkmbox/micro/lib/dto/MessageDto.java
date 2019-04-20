package vkmbox.micro.lib.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Accessors(chain=true)
public class MessageDto
{
    public enum ReceiverType { user, group };
    private String sender;
    private String receiverName;
    private ReceiverType receiverType;
    private String message;
}
