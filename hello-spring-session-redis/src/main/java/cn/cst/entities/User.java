package cn.cst.entities;

import lombok.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    public String name;
    public String pass;
}
