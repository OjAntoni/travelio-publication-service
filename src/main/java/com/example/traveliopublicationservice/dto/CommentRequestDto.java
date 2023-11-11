package com.example.traveliopublicationservice.dto;

import com.example.traveliopublicationservice.model.ERefType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequestDto {
    public long refEntityId;
    public ERefType type;
    public String text;
}
