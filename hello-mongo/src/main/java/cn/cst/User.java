package cn.cst;

import lombok.*;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class User implements Serializable {
    String name;
    Integer age;
}
