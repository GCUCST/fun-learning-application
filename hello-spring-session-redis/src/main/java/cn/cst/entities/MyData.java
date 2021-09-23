package cn.cst.entities;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MyData implements Serializable {
    public String code;
    public String msg;
}
