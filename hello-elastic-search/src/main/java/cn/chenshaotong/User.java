package cn.chenshaotong;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public String name;
    public Integer age;
    public String desc;
    public String sex;
}
