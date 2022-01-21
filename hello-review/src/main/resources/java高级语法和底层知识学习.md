











java面试题收藏:

https://blog.csdn.net/weixin_48272905/article/details/109274687

# 一.基础(Java开发)
3. 

## 3.==和equals的区别

```
==的规则是定死的,比较的是两个变量的值是否相同,基本数据类型比较的是两者的值,引用类型比较的是两者的引用所指向内存中的对象是否一致
```

## 4.(大厂面试题)HashCode和equals关系

```java
两者的作用都是用来比较两个对象是否相等
(1)HashCode是通过哈希算法得出，用来指定对象在哈希表中的索引值,在对象的数量较大的时候,可能会出现两个对象哈希值一致,即哈希冲突,这时候用hashCode来判断两个对象是否相等会出现错误,所以要使用equals
(2)在Map类型的集合中,添加元素的,确保map的key是唯一的源码就是先检查hashCode值是否相等，再通过equals比较,所以官方推荐重写了equals方法也要重写HashCode方法
源码:
public V put(K key, V value) {
        if (table == EMPTY_TABLE) { //是否初始化
            inflateTable(threshold);
        }
        if (key == null) //放置在0号位置
            return putForNullKey(value);
        int hash = hash(key); //计算hash值
        int i = indexFor(hash, table.length);  //计算在Entry[]中的存储位置
        for (Entry<K,V> e = table[i]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                V oldValue = e.value;
                e.value = value;
                e.recordAccess(this);
                return oldValue;
            }
        }

        modCount++;
        addEntry(hash, key, value, i); //添加到Map中
        return null;
}
```

## 5.(大厂面试题)深拷贝和浅拷贝的区别

```
两者都是新创建一个对象:
浅拷贝的对象中引用类型的属性值被改变后,原来的对象相应属性的值也会改变，即浅拷贝出来的对象的属性的引用指向同一个地址,深拷贝的指向不同地址

```

浅拷贝实现：

```java
继承clonable接口
public class Student implements Cloneable {

    //引用类型
    private Subject subject;
    //基础数据类型
    private String name;
    private int age;

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     *  重写clone()方法
     * @return
     */
    @Override
    public Object clone() {
        //浅拷贝
        try {
            // 直接调用父类的clone()方法
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "[Student: " + this.hashCode() + ",subject:" + subject + ",name:" + name + ",age:" + age + "]";
    }
}
```

深拷贝实现:

```java
继承clonable接口并对引用类型的属性进行拷贝
public class Student implements Cloneable {

    //引用类型
    private Subject subject;
    //基础数据类型
    private String name;
    private int age;

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     *  重写clone()方法
     * @return
     */
    @Override
    public Object clone() {
        //深拷贝
        try {
            // 直接调用父类的clone()方法
            Student student = (Student) super.clone();
            student.subject = (Subject) subject.clone();
            return student;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "[Student: " + this.hashCode() + ",subject:" + subject + ",name:" + name + ",age:" + age + "]";
    }
}
```

以上深拷贝有弊端:有多少个引用类型的属性,就要重写多少个引用类型的clone方法，代码量会比较大，可采用序列化来进行优化

```java0
    public Object deepCLone() throws IOException, ClassNotFoundException {
        //序列化
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();
    }.
```

## 6.(大厂面试题)java中的异常体系

```
java中的异常划分为两类Error和Exception:都是继承Throwable：
(1)Error是java代码运行过程不可处理的错误,Error一抛出，jvm将终止进程:
  NoClassFoundError(类定义错误)
  OutOfMemoryError(内存溢出异常)
  StackOverFlowError(栈溢出异常)
(2)Exception是程序本身可捕获并处理的异常:
有两大类:
RuntimeExcetion:运行时异常,运行时才会产生
编译异常:比如IOExcetion,如果有该异常,编译时会识别,必须对异常进行处理,否则不会通过编译

```

## 7.(大厂面试题)lambda表达式中使用外部变量,为什么要final

```
lambda表达式是由匿名表达式变化而来的，其本质还是相当于调用方法,java中调用方法时变量的传递是值传递,方法内部改变值不会影响到原有的值改变,所以为了适应该特性,防止变量被篡改,外部变量需要设置为final
```

## 8.java反射的底层原理,机制,优点和缺点

(1).什么是反射

java反射机制核心是在程序运行时动态加载类对象并获取类的信息,本质是jvm获得class对象后,再通过对象反编译,获取对象各种信息,普通获取对象的方式是通过编译生成.class文件,jvm再加载.class文件获取类的信息,在jvm中生成一个对象

(2)反射优点

<1>可通过配置文件动态加载所需要的的类,而不需要在编译前去new一个对象,对项目进行了解耦

<2>可以通过反射访问java的方法,属性,包括私有的(需要setAccessible(true)跳过权限检查)

(3)反射效率低的原因

<1>Method的invoke方法要对参数进行装箱和拆箱

```
invoke方法传参是Object[]类型,如果方法参数是简单类型,需要装箱为Object类型,比如Long在java编译的时候,用了Long.valueOf()转型,并且封装为Objcet数组,然后生成对象字节码时,因为需将参数传入方法,需要将参数数组拆箱,恢复到没有被Objcet[]包装前的样子,装箱和拆箱造成额外的内存浪费
```

<2>需要检查方法的可见性

![image-20210406233959054](C:\Users\hasee\AppData\Roaming\Typora\typora-user-images\image-20210406233959054.png)

```
每次调用invoke都需要检查方法的可见性
```

<3>反射时需要校验参数

```
invoke时需要检查每个实际参数和形参的类型是否匹配
```

<4>反射方法难以内联

<4>每次调用getMethod方法会对method对象做一次拷贝

```
    @CallerSensitive
    public Method getMethod(String name, Class<?>... parameterTypes)
        throws NoSuchMethodException, SecurityException {
        Objects.requireNonNull(name);
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            checkMemberAccess(sm, Member.PUBLIC, Reflection.getCallerClass(), true);
        }
        Method method = getMethod0(name, parameterTypes);
        if (method == null) {
            throw new NoSuchMethodException(methodToString(name, parameterTypes));
        }
        return getReflectionFactory().copyMethod(method);
    }
```



## 9.java强引用,软应用,弱引用,虚引用

https://blog.csdn.net/jiahao1186/article/details/81591861

## 10.类的初始化过程

(1)静态初始化(先父类，再子类),先静态代码块再到属性

(2)给非静态属性赋值(非静态初始化),先非静态代码块再到属性赋值

(3)调用构造函数

- 第二部和第三部在执行new,或反射创建对象的时候才会执行

```java
    static class Animal{
        private static int x1 = show("animal static");
        private int first = 9;
        protected int second;
        public Animal(){
            System.out.println("first" + first + "second" + second);
            second = 39;
        }

        static int show(String str){
            System.out.println(str);
            return 47;
        }
    }

    static class Dog extends Animal{
        private static int x2 = Animal.show("static Dog");
        private int third = Animal.show("dog third");
        public Dog(){
            System.out.println("third" + third);
            System.out.println("second" + second);
        }

        public static void main(String[] args) {
            System.out.println("main start");
            Dog dog = new Dog();
            System.out.println("main end");
        }
    }
```

## 11.try,catch,finally的坑

原理

https://blog.csdn.net/zzz_zjz/article/details/114107716

```java
    public static void main(String[] args) {
        try{
            System.out.println("aa:"+func());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static int func() throws Exception{
        int a =3;
        for(int i = 0;i< 2;i++){
            try{
                throw new Exception("bb");
            }catch (Exception ex){
                throw ex;
            }finally {
                continue;
            }
        }
        return 0;
    }
```

以上代码输出aa:0

只要有finally，无论如何都会执行,finally中的执行语句会覆盖其他语句,如上代码,catch中throw理应跳出方法,,但是finally中执行了continue让代码继续执行了

```java
    static int func(){
        int a =3;
        for(int i = 0;i< 2;i++){
            try{
                throw new Exception("bb");
            }catch (Exception ex){
                return 2;
            }finally {
                return 3;
            }
        }
        return 0;
    }
```

上面代码输出3

## 12.JDK1.8的新特性

## 13.Cglib和jdk动态代理的区别

1.jdk动态代理利用拦截器(实现InvocationHandler)和反射机制生成一个代理接口的匿名类,在调用具体方法前调用InvokeHandler去处理

2.cglib动态代理是对代理对象类生成的class文件加载进来,通过修改字节码生成子类来处理

```
如果对象有接口,使用jdk动态代理,没有实现则用cglib
```

## 14.双亲委派模型和打破双亲委派

1.**双亲委派的沙箱机制**

```
当创建一个自定义String类,在加载自定义String类的时候率先使用引导类加载器加载,引导类加载器在加载过程中会先加载jdk自带的文件(rt.jar包中的java.lang.String),报错信息说没有main方法,就是加载的是rt.jar中的String类,这样可以保证对java核心代码的保护,就是沙箱安全机制
```

**2.双亲委派优势**

- 避免类的重复加载
- 保护程序安全,防止核心API被随意篡改

**3.打破双亲委派模型**

可以继承ClassLoader类去重写loadClass方法和FindClass方法

方法	         功能	          说明
loadClass	加载类	ClassLoader的方法，会调用findClass方法
findClass	查找类	找到类的资源路径，会调用defineClass方法，在URLClassLoader中重写了该方法
defineClass	将类读到内存，并转换成class对象	URLClassLoader中的defineClass方法通过nio的方式加载class文件资源，读到内存中，并会调用ClassLoader的defineClass方法；ClassLoader的defineClass方法将内存的字节数组转成class对象

4.**为什么要有线程上下文类加载器**

在没有指定线程上下文加载器的情况下，线程将继承父线程的上下文类加载器。Java 应用运行的初始线程的上下文类加载器是**应用类加载器**，所以在不指定的情况下就默认是 **应用类加载器**。

**5.类加载器的命名空间**

每个类加载器对应一个命名空间,命名空间起到一个类相互隔离的作用

- 同一个命令空间内的类相互可见
- 子加载器的命令空间包含所有父加载器的命名空间,因此由子加载器加载的类可以看见父加载器加载的类
- 由父加载器加载的类不能看见子加载器加载的类
- 如果两个加载器之间没有直接或间接的父子关系,那么各自加载的类相互不可见

**6.Class.forName和ClassLoader.loadClass**

- Class.forName返回的Class对象可以决定是否初始化,默认开启初始化,ClassLoader.loadClass返回的类型不会初始化,只会做连接操作
- Class.forName可以决定由哪个CLassLoader来请求这个类型.而ClassLoader.loadClass是用当前的classLoader去请求

**7.线程上下文类加载器打破双亲委派模型**

Spi是java提供的一套用来被第三方实现或者扩展的接口,可以用来启用框架扩展,所有涉及Spi的加载动作基本上都采用这种方式,例如JNDI,JDBC,JCE,JAXB,JBI等

- 以JDBC为例说明该打破双亲委派模型

  java给数据库提供访问入口DiriverManager,来实例化不同的Dirder,DriverManager需要启动类加载器去加载,但是不同厂家的mysql实现类Driver不在启动类加载器的范围,而在应用程序类加载器范围,父级加载器无法加载子级类加载器路径中的类,所以使用了Thread.CurrentThread.getContextClassLoder()获取上下文加载器,默认是AppClassLoader,子线程如果没有设置,则继承父线程的,然后使用Class.forName传入上下文类加载器实例去加载类

**8.OSGI实现模块化热部署,使用自定义类加载器打破双亲委派**

OSGI每个模块都有自己独立的classpath.采用自定义不同的类加载器实现

- 1.OSGI为每个bundle提供一个类加载器,该加载器能看到bunble jar文件内部的类和资源
- 为了让bundle相互协作,基于依赖关系,从一个bundle类加载器委托到另一个bundle类加载器（组合模式）
- 当需要更换一个bundle，就把bundle连同类加载器一起换掉,实现代码热部署

9.tomcat打破双亲委派模型

![image.png](https://s2.51cto.com/images/20200728/1595936173114851.png?x-oss-process=image/watermark,size_16,text_QDUxQ1RP5Y2a5a6i,color_FFFFFF,t_100,g_se,x_10,y_10,shadow_90,type_ZmFuZ3poZW5naGVpdGk=)

tomcat实现了四个自己的类加载器,Catania类加载器负责加载catania.sh中指定的启动类,Shared类加载器负责加载tomcat/lib下共享的类库,catania和shared类加载器遵循双亲委派模型

而部署在tomcat/webapps下的*.war包项目,由WebAppClassLoader类加载器加载,等它加载不到的时候，交给上层ClassLoader进行加载,这个加载器用来隔绝不同应用的.class文件，比如两个应用,依赖同一个第三方不同版本,相互没有影响

- tomcat类加载器实现机制的目的

  1.为了实现隔离性,对于各个webapp中的class和lib相互隔离

  2.对于许多应用,需要有共享的lib以便不浪费资源

  3.考虑到安全性,使用单独classLoader装在tomcat自身类库,以免其他恶意或无意的破坏

  4.热部署,tomcat不用重启就自动重新装载类库,要怎么实现jsp文件的热加载呢？ jsp 文件其实也就是class文件，那么如果修改了，但类名还是一样，类加载器会直接取方法区中已经存在的，修改后的jsp是不会重新加载的。那么怎么办呢？可以直接卸载掉这jsp文件的类加载器 .当一个jsp文件修改了，就直接卸载这个jsp类加载器。重新创建类加载器，重新加载jsp文件。 源码详见： org.apache.jasper.servlet.JasperLoader

## 15.Java的类加载机制

## 16.单例模式如何防止反射和序列化漏洞

一、懒汉式单例模式，解决反射和反序列化漏洞

```
package com.iter.devbox.singleton;  
  
import java.io.ObjectStreamException;  
import java.io.Serializable;  
  
/** 
 * 懒汉式（如何防止反射和反序列化漏洞） 
 * @author Shearer 
 * 
 */  
public class SingletonDemo6 implements Serializable{  
      
    // 类初始化时，不初始化这个对象（延迟加载，真正用的时候再创建）  
    private static SingletonDemo6 instance;  
      
    private SingletonDemo6() {  
        // 防止反射获取多个对象的漏洞  
        if (null != instance) {  
            throw new RuntimeException();  
        }  
    }  
      
    // 方法同步，调用效率低  
    public static synchronized SingletonDemo6 getInstance() {  
        if (null == instance)  
            instance = new SingletonDemo6();  
        return instance;  
    }  
  
    // 防止反序列化获取多个对象的漏洞。  
    // 无论是实现Serializable接口，或是Externalizable接口，当从I/O流中读取对象时，readResolve()方法都会被调用到。  
    // 实际上就是用readResolve()中返回的对象直接替换在反序列化过程中创建的对象。  
    private Object readResolve() throws ObjectStreamException {    
        return instance;  
    }  
}  
  
  
package com.iter.devbox.singleton;  
  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.ObjectInputStream;  
import java.io.ObjectOutputStream;  
  
public class Client2 {  
  
    public static void main(String[] args) throws Exception {  
        SingletonDemo6 sc1 = SingletonDemo6.getInstance();  
        SingletonDemo6 sc2 = SingletonDemo6.getInstance();  
        System.out.println(sc1); // sc1，sc2是同一个对象  
        System.out.println(sc2);  
          
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此漏洞）  
/*      Class<SingletonDemo6> clazz = (Class<SingletonDemo6>) Class.forName("com.iter.devbox.singleton.SingletonDemo6"); 
        Constructor<SingletonDemo6> c = clazz.getDeclaredConstructor(null); 
        c.setAccessible(true); // 跳过权限检查 
        SingletonDemo6 sc3 = c.newInstance(); 
        SingletonDemo6 sc4 = c.newInstance(); 
        System.out.println(sc3);  // sc3，sc4不是同一个对象 
        System.out.println(sc4);*/  
          
        // 通过反序列化的方式构造多个对象（类需要实现Serializable接口）  
          
        // 1. 把对象sc1写入硬盘文件  
        FileOutputStream fos = new FileOutputStream("object.out");  
        ObjectOutputStream oos = new ObjectOutputStream(fos);  
        oos.writeObject(sc1);  
        oos.close();  
        fos.close();  
          
        // 2. 把硬盘文件上的对象读出来  
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("object.out"));  
        // 如果对象定义了readResolve()方法，readObject()会调用readResolve()方法。从而解决反序列化的漏洞  
        SingletonDemo6 sc5 = (SingletonDemo6) ois.readObject();  
        // 反序列化出来的对象，和原对象，不是同一个对象。如果对象定义了readResolve()方法，可以解决此问题。  
        System.out.println(sc5);   
        ois.close();  
    }  
  
}
```

**二、静态内部类式单例模式（解决反射和反序列化漏洞）**

```
package com.iter.devbox.singleton;  
  
import java.io.ObjectStreamException;  
import java.io.Serializable;  
  
/** 
 * 静态内部类实现方式（也是一种懒加载方式） 
 * 这种方式：线程安全，调用效率高，并且实现了延迟加载 
 * 解决反射和反序列化漏洞 
 * @author Shearer 
 * 
 */  
public class SingletonDemo7 implements Serializable{  
      
    private static class SingletonClassInstance {  
        private static final SingletonDemo7 instance = new SingletonDemo7();  
    }  
      
    // 方法没有同步，调用效率高  
    public static SingletonDemo7 getInstance() {  
        return SingletonClassInstance.instance;  
    }  
      
    // 防止反射获取多个对象的漏洞  
    private SingletonDemo7() {  
        if (null != SingletonClassInstance.instance)  
            throw new RuntimeException();  
    }  
      
    // 防止反序列化获取多个对象的漏洞  
    private Object readResolve() throws ObjectStreamException {    
        return SingletonClassInstance.instance;  
    }  
}  
  
  
package com.iter.devbox.singleton;  
  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.ObjectInputStream;  
import java.io.ObjectOutputStream;  
import java.lang.reflect.Constructor;  
  
public class Client3 {  
  
    public static void main(String[] args) throws Exception {  
        SingletonDemo7 sc1 = SingletonDemo7.getInstance();  
        SingletonDemo7 sc2 = SingletonDemo7.getInstance();  
        System.out.println(sc1); // sc1，sc2是同一个对象  
        System.out.println(sc2);  
          
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此漏洞）  
        Class<SingletonDemo7> clazz = (Class<SingletonDemo7>) Class.forName("com.iter.devbox.singleton.SingletonDemo7");  
        Constructor<SingletonDemo7> c = clazz.getDeclaredConstructor(null);  
        c.setAccessible(true); // 跳过权限检查  
        SingletonDemo7 sc3 = c.newInstance();  
        SingletonDemo7 sc4 = c.newInstance();  
        System.out.println("通过反射的方式获取的对象sc3：" + sc3);  // sc3，sc4不是同一个对象  
        System.out.println("通过反射的方式获取的对象sc4：" + sc4);  
          
        // 通过反序列化的方式构造多个对象（类需要实现Serializable接口）  
          
        // 1. 把对象sc1写入硬盘文件  
        FileOutputStream fos = new FileOutputStream("object.out");  
        ObjectOutputStream oos = new ObjectOutputStream(fos);  
        oos.writeObject(sc1);  
        oos.close();  
        fos.close();  
          
        // 2. 把硬盘文件上的对象读出来  
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("object.out"));  
        // 如果对象定义了readResolve()方法，readObject()会调用readResolve()方法。从而解决反序列化的漏洞  
        SingletonDemo7 sc5 = (SingletonDemo7) ois.readObject();  
        // 反序列化出来的对象，和原对象，不是同一个对象。如果对象定义了readResolve()方法，可以解决此问题。  
        System.out.println("对象定义了readResolve()方法，通过反序列化得到的对象：" + sc5);   
        ois.close();  
    }  
  
}
```

## 17.多路复用select,poll,epoll区别

- fd指**文件描述符**

- select

  - 时间复杂度O(n)
  - 无差别轮询所有流，找出能读出数据，或者写入数据的流，对他们进行操作

- poll

  - 时间复杂度O(n)
  - poll本质上和select没有区别，
  - 它将用户传入的数据拷贝到内核空间，然后查询每个fd对应的设备状态， 
  - **但是它没有最大连接数的限制**，原因是它是基于链表来存储的.

- epoll

  - 时间复杂度O(1)

  - **可以理解为event poll**

  - epoll实际上是

    事件驱动（每个事件关联上fd）


**但select，poll，epoll本质上都是同步I/O，**

- **因为他们都需要在读写事件就绪后自己负责进行读写，**
- **也就是说这个读写过程是阻塞的**，
- 而异步I/O则无需自己负责进行读写，**异步I/O**的实现**会负责**把数据从**内核拷贝到用户空间**。

- AIO也是基于epoll，
  - 把数据从**内核拷贝到用户空间**

## 18.Stream底层实现



# 二.集合

## 1.(大厂面试题)Collection有哪些子接口,有哪些具体实现

```
有两大子类:List(允许有重复元素)和set(不允许有重复元素)
(1)List实现:
ArrayList,LinkList,vector
ArrayList和LinkList：前者基于数组实现,后者基于链表实现
vector是线程安全的数组
(2)set实现
Hashset,treeset,linkedset
```

## 2.(大厂面试)ArrayList底层实现,加操作,取值操作,什么时候扩容

```java
Arralist底层是一个Objcet[]数组,加操作执行add()方法,
然后如果size==当前Object数组长度会执行grow:
private void add(E e, Object[] elementData, int s) {
    if (s == elementData.length)
    elementData = grow();
    elementData[s] = e;
    size = s + 1;
}
private Object[] grow() {
    return grow(size + 1);
}
private Object[] grow(int minCapacity) {
    int oldCapacity = elementData.length;
    if (oldCapacity > 0 || elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
    int newCapacity = ArraysSupport.newLength(oldCapacity,
    minCapacity - oldCapacity, /* minimum growth */
    oldCapacity >> 1           /* preferred growth */);
    return elementData = Arrays.copyOf(elementData, newCapacity);
    } else {
    return elementData = new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];
}
}
由上面代码可知：第一次执行add时,如果ArrayList是空的,会生成一个长度为10的数组，后面如果size到达当前Object数组的长度时,会走if逻辑进行扩容
```

## 3.(大厂面试)HashMap原理,HashMap是否可并发读,并发写会有什么问题

```
哈希算法就是把任意长度的值通过散列算法换成固定长度的值,通过这个值作为位置来访问
```

```
https://blog.csdn.net/visant/article/details/80045154
HashMap底层是一个数组加链表,数组分为一个一个桶,每个桶存放一个链表,链表中存放的就是键值对,当链表树超过树转换的阈值,链表就会变为红黑树的结构
```

```
(1)高并发情况下如果同时写和读会出现问题
get的时候会通过(哈希数组长度 - 1) & hash来确定索引值,put数据进来时,如果哈希数组发生扩容,这个值会有偏差
(2)单单高并发读不会产生问题
(3)高并发写的情况由于链表的头插法导致HashMap的Resize方法形成链表环,程序再调用get会进入死循环(jdk1.7),jdk1.8已修复(使用尾插法),但改完后高并发写虽然没问题,但高并发下修改里面的内容仍然会有问题
```

1.put方法:

第一次执行put方法会初始化数组,数组长度默认16

```
    /**
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
```

如果指定默认容量,HashMap也会将数组初始化为一个大于或等于默认容量的2的整数次幂,代码如下

```
/**
 * Constructs an empty {@code HashMap} with the specified initial
 * capacity and the default load factor (0.75).
 *
 * @param  initialCapacity the initial capacity.
 * @throws IllegalArgumentException if the initial capacity is negative.
 */
public HashMap(int initialCapacity) {
    //DEFAULT_LOAD_FACTOR是个
    this(initialCapacity, DEFAULT_LOAD_FACTOR);
}
```



## 4.(大厂面试)集合在迭代的过程中，插入或删除数据会怎样

```java
import java.util.*;
public class test{
    public static void main(String[] args){
        List<Integer> number = new LinkedList<>();
        number.addAll(Arrays.asList(10, 9, 8, 7, 6, 5, 4, 3, 2, 1));
        for (Integer i : number) {
            if (i > 5)
                number.remove(i);
        }
        System.out.println(number.toString());
    }
}
```

上面代码执行会抛出异常:

```java
Exception in thread "main" java.util.ConcurrentModificationException
        at java.util.LinkedList$ListItr.checkForComodification(LinkedList.java:966)
        at java.util.LinkedList$ListItr.next(LinkedList.java:888)
        at test.main(test.java:6)
```

原因如下:

```
创建时，就将modCount赋值给了expectedModCount
然后在迭代器开始迭代，即调用next()这个函数的时候，都会调用一次内部的checkForComodification()函数
这个函数如下：
final void checkForComodification() {
    if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
}
modCount是原来集合的版本号,当集合增加或删除元素的时候modCount会加一,expectedModCount是当前迭代集合的版本号，不会随着modCount更改而变化,所以就会出现两个值不等的情况
```

解决方法:

使用迭代器内部的remove方法

```java
List<Integer> number = new LinkedList<>();
number.addAll(Arrays.asList(10, 9, 8, 7, 6, 5, 4, 3, 2, 1));
Iterator<Integer> iterator = number.iterator();
while (iterator.hasNext()){
    Integer i = iterator.next();
    if (i>5){
        iterator.remove();
    }
}
System.out.println(number.toString());
```

## 5.(大厂面试题)concurrentHashMap 原理。头插法还是尾插法？扩容怎么做

https://blog.csdn.net/ym123456677/article/details/78860719

JDK1.7之前:

concurrentHashMap的结构如下:

![img](https://img-blog.csdn.net/20180521171105146?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3ltMTIzNDU2Njc3/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)



```
ConcurrentHashMap初始化时，计算出Segment数组的大小ssize和每个Segment中HashEntry数组的大小cap，并初始化Segment数组的第一个元素；其中ssize大小为2的幂次方，默认为16，cap大小也是2的幂次方，最小值为2，最终结果根据根据初始化容量initialCapacity进行计算，其中Segment在实现上继承了ReentrantLock，这样就自带了锁的功能

当执行put方法插入数据时，根据key的hash值，在Segment数组中找到相应的位置，如果相应位置的Segment还未初始化，则通过CAS进行赋值，接着执行Segment对象的put方法通过加锁机制插入数据。
1、线程A执行tryLock()方法成功获取锁，则把HashEntry对象插入到相应的位置；
2、线程B获取锁失败，则执行scanAndLockForPut()方法，在scanAndLockForPut方法中，会通过重复执行tryLock()方法尝试获取锁，在多处理器环境下，重复次数为64，单处理器重复次数为1，当执行tryLock()方法的次数超过上限时，则执行lock()方法挂起线程B；
3、当线程A执行完插入操作时，会通过unlock()方法释放锁，接着唤醒线程B继续执行

size实现
因为ConcurrentHashMap是可以并发插入数据的，所以在准确计算元素时存在一定的难度，一般的思路是统计每个Segment对象中的元素个数，然后进行累加，但是这种方式计算出来的结果并不一样的准确的，因为在计算后面几个Segment的元素个数时，已经计算过的Segment同时可能有数据的插入或则删除，在1.7的实现中，采用了如下方式：

先采用不加锁的方式，连续计算元素的个数，最多计算3次：
1、如果前后两次计算结果相同，则说明计算出来的元素个数是准确的；
2、如果前后两次计算结果都不同，则给每个 Segment 进行加锁，再计算一次元素的个数

```

jdk1.8实现:

```
取消了jdk1.7的segment分段锁的数据结构,使用数组+链表(红黑树)的结构,而对于锁的粒度，调整为了对每个数组元素加锁，使用node+CAS+Synchronized,另外定位节点的hash算法被简化了,hash冲突会加剧因此在链表节点数大于8时会将链表转化为红黑树进行存储.查询时间复杂度由原先的O(n)变为O(logn)
```

基本结构:

![img](https://img-blog.csdn.net/20180522155453418?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3ltMTIzNDU2Njc3/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

1.插入、调用putVal，第一次put的时候会执行初始化容器,通过cas机制

![image-20210320204419711](C:\Users\hasee\AppData\Roaming\Typora\typora-user-images\image-20210320204419711.png)

initTable方法

```java
private final Node<K,V>[] initTable() {
    Node<K,V>[] tab; int sc;
    while ((tab = table) == null || tab.length == 0) {
        //sizeCtl用来标志是否有线程在初始化,如果<0，说明在初始化,让出cpu执行权
        if ((sc = sizeCtl) < 0)
            Thread.yield(); // lost initialization race; just spin
        //这里执行cas来判断是否有线程在进行初始化
        else if (U.compareAndSetInt(this, SIZECTL, sc, -1)) {
            try {
                if ((tab = table) == null || tab.length == 0) {
                    int n = (sc > 0) ? sc : DEFAULT_CAPACITY;
                    @SuppressWarnings("unchecked")
                    Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n];
                    table = tab = nt;
                    sc = n - (n >>> 2);
                }
            } finally {
                sizeCtl = sc;
            }
            break;
        }
    }
    return tab;
}
```

2.如果已经初始化,且插入的问题为空则执行casTabAt通过cas机制进行更新值

![image-20210320224223949](C:\Users\hasee\AppData\Roaming\Typora\typora-user-images\image-20210320224223949.png)

```java
static final <K,V> boolean casTabAt(Node<K,V>[] tab, int i,
                                    Node<K,V> c, Node<K,V> v) {
    return U.compareAndSetReference(tab, ((long)i << ASHIFT) + ABASE, c, v);
}
```

3.如果相应位置的`Node`不为空，且当前该节点不处于移动状态，则对该节点加`synchronized`锁，如果该节点的`hash`不小于0(说明没有在移动)，则遍历链表更新节点或插入新节点

使用的是尾插法

```java
if (fh >= 0) {
    binCount = 1;
    //f是链表的头
    for (Node<K,V> e = f;; ++binCount) {
        K ek;
        if (e.hash == hash &&
            ((ek = e.key) == key ||
             (ek != null && key.equals(ek)))) {
            oldVal = e.val;
            if (!onlyIfAbsent)
                e.val = value;
            break;
        }
        Node<K,V> pred = e;
        if ((e = e.next) == null) {
            pred.next = new Node<K,V>(hash, key, value);
            break;
        }
    }
}
```

4.如果该节点是`TreeBin`类型的节点，说明是红黑树结构，则通过`putTreeVal`方法往红黑树中插入节点

```java
else if (f instanceof TreeBin) {
    Node<K,V> p;
    binCount = 2;
    if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                   value)) != null) {
        oldVal = p.val;
        if (!onlyIfAbsent)
            p.val = value;
    }
}
```

5.如果`binCount`不为0，说明`put`操作对数据产生了影响，如果当前链表的个数达到8个，则通过`treeifyBin`方法转化为红黑树，如果`oldVal`不为空，说明是一次更新操作，没有对元素个数产生影响，则直接返回旧值

```java
if (binCount != 0) {
    if (binCount >= TREEIFY_THRESHOLD)
        treeifyBin(tab, i);
    if (oldVal != null)
        return oldVal;
    break;
}
```

- 扩容:https://blog.csdn.net/qq_19636353/article/details/103137354

  1.当数组长度小于64且某个链表的数大于8则扩容

  2.当发现其他线程扩容时，帮其扩容

  3.当前容量超过阈值

## 6.(面试题)List如何删除数据

List删除成功原理:List调用remove(index)方法后，会移除index位置上的元素，index之后的元素就全部依次左移

- 注意:

  如果需要remove自定义对象,如果对象重写了hashCode方法和equals方法,当改变了对象的属性时调用集合的remove方法将无法删除元素,因为改变了属性后对象的hashCode会变化,然后remove对象的时候,会通过比较集合中的对象是否和你的对象相同,调用的是equals方法

### (1).正序删除

如果只删除至多1个元素，那只需要在删除后使用break语句跳出循环即可，如果需要删除多个元素，需要控制index,若不注意控制当前列表的size和下一个元素的index，容易报异常

```java
public static void remove(List<String> list, String target) {
    for(int i = 0, length = list.size(); i < length; i++){
        String item = list.get(i);
        if(target.equals(item)){
            list.remove(item);
            length--;
            i--;
        }
    }
}
```

### (2).反序删除

倒序删可以克服正序删需要额外管理列表size和下一个元素的index的问题，使用起来也很方便

```java
public static void remove(List<String> list, String target) {
    for(int i = list.size() - 1; i >= 0; i--){
        String item = list.get(i);
        if(target.equals(item)){
            list.remove(item);
        }
    }
}
```

### (3).迭代器remove()方法删除（推荐）

```java
public static void remove(List<String> list, String target) {
    Iterator<String> iter = list.iterator();
    while (iter.hasNext()) {
        String item = iter.next();
        if (item.equals(target)) {
            iter.remove();
        }
    }
}
```

迭代器remove()方法虽然方便，但仍有需要注意的地方，要用此法删除元素的前提是该 List 的实现类的iterator()方法返回的Iterator实现类支持remove()方法，否则会报 *java.lang.UnsupportedOperationException*异常，常用的ArrayList的Iterator支持remove()方法，但有些情况下就会有问题，如下

```java
Integer[] arr = {1, 2, 3, 4, 5};
List<Integer> list = Arrays.asList(arr);//没有提供add()和remove()方法
Iterator<Integer> iter = list.iterator();
while (iter.hasNext()) {
    Integer item = iter.next();
    if (item == 2) {
        iter.remove();
    }
}
//这种情况就会运行失败，报 java.lang.UnsupportedOperationException异常
```

```java
List<Integer> list = new ArrayList<>();
list.add(1);
list.add(2);
list.add(3);
Iterator<Integer> iter = list.iterator();
while (iter.hasNext()) {
	iter.remove();
}
//如果想用这种方法删除List所有元素，则会报java.lang.IllegalStateException异常，原因就是没有在删除前调用Iterator的next()方法
```

```java
List<String> list = new ArrayList<>();
list.add("a");
list.add("b");
list.add("c");
Iterator<String> iter = list.iterator();
while (iter.hasNext()) {
	String item = iter.next();
    if (item.equals("a") || item.equals("c")) {
        list.remove(item);
    }
}
//注意，上面的代码调用了List的remove()而不是Iterator的remove()，如果只删除一个元素，那么在删除后调用break语句即可，但这里目的是删除多于1个的元素，会报java.util.ConcurrentModificationException异常
```

### (4).增强for循环删除

增强for循环中删除元素后继续循环会报 *java.util.ConcurrentModificationException* 异常，因为元素在使用的时候发生了并发的修改，导致异常抛出，但是删除完毕马上使用break语句跳出循环，则不会触发报错，所以它适合删除至多1个元素

```java
public static void remove(List<String> list, String target) {
    for (String item : list) {
        if (item.equals(target)) {
            list.remove(item);
            break;
        }
    }
}
```



### (5).使用stream 删除

Java8引入的stream API带来了新的比较简洁的删除List元素的方法filter，该方法不会改变原List对象，须返回新的对象，下面的例子演示了如何使用stream删除集合中的 "*" 元素。

```java
List<String> list = new ArrayList<>();
list.add("a");
list.add("b");
list.add("*");
list.add("c");
list.add("*");
List<String> result = list.stream().filter(item -> !"*".equals(item)).collect(Collectors.toList());
```



### (6).CopyOnWriteArrayList线程安全删除

利用 CopyOnWrite容器。CopyOnWrite容器即写时复制的容器。通俗的理解是当我们往一个容器添加元素的时候，不直接往当前容器添加，而是先将当前容器进行Copy，复制出一个新的容器，然后新的容器里添加元素，添加完元素之后，再将原容器的引用指向新的容器。这样做的好处是我们可以对CopyOnWrite容器进行并发的读，而不需要加锁，因为当前容器不会添加任何元素。所以CopyOnWrite容器也是一种读写分离的思想，读和写不同的容器

```java
public static List<String> remove(ArrayList<String> list, String target) {
    CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<String>(list);
    for (String item : cowList) {
        if (item.equals(target)) {
            cowList.remove(item);
        }
    }
    return cowList;
}
```

- 注意

1. 使用CopyOnWriteArrayList的好处是我们不仅仅可以删除，也可以在遍历的得时候添加新元素。
2. 以上方法并没有修改参数list，而是返回CopyOnWriteArrayList给调用者，也就是说CopyOnWriteArrayList并不修改构造它的List，而是自己内部维护着一个List，这一点要特别注意。
3. CopyOnWriteArrayList不是ArrayList的子类，但它实现了List接口。s



# 三.多线程

## 1.(大厂面试)进程和线程区别,协程是什么,进程之间如何通信,进程 A 想读取进程 B 的主存怎么办

(1)进程,线程,协程的区别

```
1.进程有独立的地址空间,一个进程挂掉不会对其他进程产生影响,是操作系统资源分配的最小单位，有自己独立的内存地址,
2.线程是一个进程中的不同执行路径,内存是共享的,无独立内存地址，有自己的栈空间,是cpu调度的最小单位.
3.协程比线程更轻量,不被操作系统内核管理,而完全由程序控制,带来好处就是减少cpu的开销，多线程会有操作系统层面的上下文切换,协程无锁的机制,因为协程是在同一个线程下,变量是共享的,而且，各个协程之间是串行的,无资源冲突
```

(2) 进程之间的通信方式

首先了解进程间通信的目的:

```
(1)数据的传输
(2)资源的共享
多个进程共享同一个资源
(3)消息的通知
一个进程向另一个进程发送消息，通知他某个事件
(4)进程控制
一个进程要完全控制另外一个进程，拦截另外一个进程的异常
```

进程间的通信方式:

```
1.管道(pipe)
管道是把一个程序的输出直接连接到另一个程序的输入,类似于linux里面ls -l | grep aa的原理
2.共享内存
3.套接字(socket)
```



## 2.(大厂面试)线程间通信？线程的生命周期有哪些状态？怎么转换？

(1)线程间通信:

```
1.join
join底层是wait，使用时以当前的线程为锁，会释放当前线程对象的锁
当前线程让出cpu执行权,等待另一个线程完成执行
public final synchronized void join(final long millis)
    throws InterruptedException {
        if (millis > 0) {
            if (isAlive()) {
                final long startTime = System.nanoTime();
                long delay = millis;
                do {
                    wait(delay);
                } while (isAlive() && (delay = millis -
                        TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)) > 0);
            }
        } else if (millis == 0) {
            while (isAlive()) {
                wait(0);
            }
        } else {
            throw new IllegalArgumentException("timeout value is negative");
        }
    }
2.wait
当前线程释放锁对象,并让出cpu执行权,等待其他线程唤醒
3.notify
notify唤醒随机一个线程,notify唤醒所有线程
```

(2)线程的生命周期与切换

![img](https://img-blog.csdnimg.cn/20190305195320119.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3lhbmd5ZWNoaQ==,size_16,color_FFFFFF,t_70)

## 3.(大厂面试)wait 和 sleep 有什么区别？什么情况下会用到 sleep？怎么停止线程？怎么控制多个线程按序执行？

(1)wait和sleep的区别

```
1.wait必须放在同步代码块中使用,而Sleep能在任何地方使用
2.wait会释放锁,而sleep不会释放锁
```

(2)什么情况下会用到sleep

```
https://baijiahao.baidu.com/s?id=1599627017892605099&wfr=spider&for=pc
```

(3)如何停止线程

```
最好的办法是使用boolean类型的一个标志和interupt方法结合,java 中还有一个Thread.stop的方法，但已被废除，因为会产生不可预知的错误,比如一个线程监控某个逻辑或者更新某个变量,万一执行stop，这个线程就直接停止了，逻辑就会错误
```

(4)如何控制多个线程按顺序执行

使用join



## 4.多线程interupt方法

http://ibruce.info/2013/12/19/how-to-stop-a-java-thread/

## 5.cas底层原理，乐观锁和悲观锁

https://blog.csdn.net/demo_gsl/article/details/80993306

```
cas:CompareAndSwap
cas是一种乐观锁,乐观锁就是没有冲突去完成某项动作,如果有冲突就重试,直到成功为止

cas有3个操作数:内存值,旧预期值,要修改的新增
通过比较内存值和旧预期值是否一致,如果一致就将内存值修改为要修改的新值,不一致则不断循环判断,保证了原子性,但是会出现ABA的问题,就一个变量A被其他线程改为B后又变为A，但是cas检测到A没有发生改变,解决措施就是给变量加上版本号,每次变量更新把版本号加一，这样就能判断原始变量是否改变
从Java1.5开始JDK的atomic包里提供了一个类AtomicStampedReference来解决ABA问题。这个类的compareAndSet方法作用是首先检查当前引用是否等于预期引用，并且当前标志是否等于预期标志
```

## 6.线程池原理和使用

- 核心思想:线程复用,减少创建线程的开销

线程池内部就是一个线程集合workerSet和一个阻塞队列workQueue。当用户向线程池提交一个任务(也就是线程)时，线程池会先将任务放入workQueue中。workerSet中的线程会不断的从workQueue中获取线程然后执行。当workQueue中没有任务的时候，worker就会阻塞，直到队列中有任务了就取出来继续执行

- 线程池几个参数的作用

```java
public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler)
```

- corePoolSize:(线程池核心线程数量,即线程池的正在运行的数量)

  线程池中会维护一个最小的线程数量，即使这些线程处理空闲状态，他们也不会被销毁，除非设置了allowCoreThreadTimeOut。这里的最小线程数量即是corePoolSize

- maximumPoolSize(线程池最大可运行的线程数量)

  一个任务被提交到线程池后，首先会缓存到工作队列,如果工作队列满了,则会创建一个新的线程,然后从工作队列中取出一个任务让新线程处理,再把提交的任务放入工作队列,但不会无限制去创建新的线程,这个最大值就是maximumPoolSize

- keepAliveTime 空闲线程的存活时间

  当一个线程处于空闲,当当前线程池参数allowCoreThreadTimeOut设置为true时,如果空闲时间超过keepAliveTime，这个线程就会销毁

- unit(keepAliveTime的时间单位)

- workQueue:线程池的工作队列,jdk提供4种工作队列

  - ArrayBlockingQueue
  
    ```
    队列维护一个数组,先进先出队列，put和add用的同一把锁,但不同condition,构造函数必须指定一个大小,如果队列已经是满的，则创建一个新线程，如果线程数量已经达到maxPoolSize，则会执行拒绝策略
    ```
  
  - LinkedBlockingQueue
  
    ```
    队列维护一个链表，先进先出队列,put和add用的同一把锁,但不同condition，构造函数可不指定大小(不指定大小的话最大元素长度是Integer.max),当线程池中线程数量达到corePoolSize后，再有新任务进来，会一直存入该队列，而不会去创建新线程直到maxPoolSize，因此使用该工作队列时，参数maxPoolSize其实是不起作用的,也可指定队列大小,当线程大小超过指定大小,maxPoolSize就生效
    ```
  
  - SynchronousQueue
  
    ```
    特殊的BlockingQueue,对其的操作必须是放和取交替完成的
    ```
  
  - PriorityBlockingQueue
  
    ```
    优先级的队列,可通过传入Comparator来指定进出队列顺序,内部维护一个数组
    ```

- threadFactory 线程工厂

```
创建一个新线程时使用的工厂,可以用来设定线程名,是否为daemon线程
```

- handler拒绝策略

  当工作队列中的任务已到达最大限制，并且线程池中的线程数量也达到最大限制，这时如果有新任务提交进来，该如何处理呢。这里的拒绝策略，就是解决这个问题的，jdk中提供了4种拒绝策略

  ①CallerRunsPolicy

     该策略下，在调用者线程中直接执行被拒绝任务的run方法，除非线程池已经shutdown，则直接抛弃任务

  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200402085007614.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTM5MTUyODY=,size_16,color_FFFFFF,t_70)

  ②AbortPolicy

     该策略下，直接丢弃任务，并抛出RejectedExecutionException异常。

    ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200402085035133.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTM5MTUyODY=,size_16,color_FFFFFF,t_70)

  ③DiscardPolicy

  ​    该策略下，直接丢弃任务，什么都不做。

  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200402085059733.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTM5MTUyODY=,size_16,color_FFFFFF,t_70)

  ④DiscardOldestPolicy

     该策略下，抛弃进入队列最早的那个任务，然后尝试把这次拒绝的任务放入队列

  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200402085129382.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTM5MTUyODY=,size_16,color_FFFFFF,t_70)

## 7.ThreadLocal作用和底层

一.ThreadLocal是什么

```
ThreadLocal是一种线程变量,ThreadLocal中装入的变量只属于当前线程,对于其他线程是隔离的.
```

https://baijiahao.baidu.com/s?id=1653790035315010634&wfr=spider&for=pc

二.使用场景

三.使用例子

```java
@Test
void contextLoads() {
    ThreadLocal<String> local = new ThreadLocal<>();
    Random random = new Random();
    //使用IntStream新建五个线程
    IntStream.range(0, 5).forEach(a -> new Thread(()->{
        local.set(a + "  " + random.nextInt(10));
        System.out.println("线程和local值分别是  " + local.get());
        try{
            TimeUnit.SECONDS.sleep(1);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }).start());
}
```

四.底层实现

(1)ThreadLocal定义以下重要的4个方法

![img](https://pics1.baidu.com/feed/5882b2b7d0a20cf4381d1f17d7f1b833aeaf9963.jpeg?token=354a91e3c84ef7cc069454f9842508b0&s=F0C0B14452F4887C1660DC0B0300E0C1)

主要看set方法,就能认识到整体的ThreadLocal

(2)Set方法

![img](https://pics4.baidu.com/feed/d788d43f8794a4c24768d2b6aa0ce8d0af6e39e5.jpeg?token=be9e163f7bb852ae622cddfed4486dec&s=B8D1A14416F0AD685ADD80110000C0C1)

- 每个Thread中都维护一个自己的ThreadLocalMap

首先获取到当前线程,然后调用getMap获取当当前线程的ThreadLocalMap,如果map存在,则将当前ThreadLocal作为Key,set传入的值作为value放到map中

(3)ThreadLocalMap实现

![img](https://pics4.baidu.com/feed/dcc451da81cb39dbc5179c0a76eefa21a9183091.jpeg?token=2022bfb126e4e2cdd0121264f6e3f3cf&s=BAC1A14C12A4BD6C4CD4D40F000070C1)

ThreadLocalMap就是ThreadLocal的一个静态内部类,里面定义了一个Entry来保存数据,继承弱引用,

```
（1）每个Thread维护着一个ThreadLocalMap的引用
（2）ThreadLocalMap是ThreadLocal的内部类，用Entry来进行存储
（3）ThreadLocal创建的副本是存储在自己的threadLocals中的，也就是自己的ThreadLocalMap。
（4）ThreadLocalMap的键值为ThreadLocal对象，而且可以有多个threadLocal变量，因此保存在map中
（5）在进行get之前，必须先set，否则会报空指针异常，当然也可以初始化一个，但是必须重写initialValue()方法。
（6）ThreadLocal本身并不存储值，它只是作为一个key来让线程从ThreadLocalMap获取value。
OK，现在从源码的角度上不知道你能理解不，对于ThreadLocal来说关键就是内部的ThreadLocalMap。
```

- ThreadLocal的内存泄漏问题

  ```
  1、Thread中有一个map，就是ThreadLocalMap
  2、ThreadLocalMap的key是ThreadLocal，值是我们自己设定的。
  3、ThreadLocal是一个弱引用，当为null时，会被当成垃圾回收
  4、重点来了，突然我们ThreadLocal是null了，也就是要被垃圾回收器回收了，但是此时我们的ThreadLocalMap生命周期和Thread的一样，它不会回收，这时候就出现了一个现象。那就是ThreadLocalMap的key没了，但是value还在，这就造成了内存泄漏。
  解决办法：使用完ThreadLocal后，执行remove操作，避免出现内存溢出情况。
  ```

  - 父子线程共享ThreadLoacl
  
    Thread中除了ThreadLocalMap还有一个inheritableThreadLocals ,Thread 的默认构造调用了 init 方法。init方法内部对inheritableThreadLocals 变量进行了初始化
  
    ```
     private void init(ThreadGroup g, Runnable target, String name, long stackSize, AccessControlContext acc, boolean inheritThreadLocals) {
          if (name == null) {
    	throw new NullPointerException("name cannot be null");
          }
          this.name = name;
          Thread parent = currentThread();
          SecurityManager security = System.getSecurityManager();
          //线程组加入逻辑
          if (g == null) {
    	//获取到安全管理器则使用安全管理器
    	if (security != null) {
    		g = security.getThreadGroup();
    	}
    	//未获取到安全管理器则加入父线程线程组
    	if (g == null) {
    	      g = parent.getThreadGroup();
    	}
          }
          g.checkAccess();
          if (security != null) {
    	if (isCCLOverridden(getClass())) {
    	      security.checkPermission(SUBCLASS_IMPLEMENTATION_PERMISSION);
    	}
          }
          g.addUnstarted();
          this.group = g;
          //是否守护线程
          this.daemon = parent.isDaemon();
          //线程权重
          this.priority = parent.getPriority();
          if (security == null || isCCLOverridden(parent.getClass()))
    	this.contextClassLoader = parent.getContextClassLoader();
          else
    	this.contextClassLoader = parent.contextClassLoader;
          this.inheritedAccessControlContext =acc != null ? acc : AccessController.getContext();
          this.target = target;
          setPriority(priority);
    
          //【注意此处代码实现】如果父线程inheritableThreadLocals 不为null，则初始化自身inheritThreadLocals。
          // ThreadLocal.createInheritedMap方法传入了父线程的inheritableThreadLocals
          // 返回的是 new ThreadLocalMap(parentMap),注意此处是引用传递。理论上也就是说子线程修改数据对父线程是可见的。
          if (inheritThreadLocals && parent.inheritableThreadLocals != null)
    	this.inheritableThreadLocals =ThreadLocal.createInheritedMap(parent.inheritableThreadLocals);
    
          //分配线程内存
          this.stackSize = stackSize;
    
          //设置线程id
          tid = nextThreadID();
    }
    ```
  
    源码中:InheritableThreadLocal是继承自ThreadLocal的。主要是覆盖了ThreadLocal的以下几个方法：
  
    ![img](https://img2020.cnblogs.com/blog/1780424/202005/1780424-20200526105634655-996879457.png)

重写了getMap方法,取得是父进程的inheratableThreadLocals,所以用InheritableThreadLocal就可以实现父子线程变量共享

案例：

```
package thread.pdd;

import java.util.stream.IntStream;

/**
 * @author:liyangpeng
 * @date:2020/5/25 11:38
 */
public class ThreadLocalTest {

    public static void main(String[] args) throws InterruptedException {

        InheritableThreadLocal<String> threadLocal=new InheritableThreadLocal<>();
        threadLocal.set("1111");

        Thread thread=new Thread(()->{
            System.out.println(threadLocal.get());
        });
        thread.start();
        Thread.sleep(500);
        System.out.println("-------------------华丽的分割线-----------------");
        //流操作|内部实现也是多线程
        IntStream.range(0,10).parallel().forEach(id->{
            System.out.println(id+"_~_~_"+threadLocal.get());
        });
    }
}

输出结果：
1111
-------------------华丽的分割线-----------------
6_~_~_1111
5_~_~_1111
1_~_~_1111
0_~_~_1111
4_~_~_1111
8_~_~_1111
3_~_~_1111
9_~_~_1111
7_~_~_1111
2_~_~_1111
```

```
package thread.pdd;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * @author:liyangpeng
 * @date:2020/5/25 11:38
 */
public class ThreadLocalTest {

    public static void main(String[] args){
        
        InheritableThreadLocal<String> threadLocal=new InheritableThreadLocal<>();
        threadLocal.set("--->哈哈哈");
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        executorService.execute(()->{
            Thread thread=Thread.currentThread();
            System.out.println(thread.getName()+"__"+threadLocal.get());
        });
        //重新修改InheritableThreadLocal内的数据
        threadLocal.set("--->呵呵呵");

        executorService.execute(()->{
            Thread thread=Thread.currentThread();
            System.out.println(thread.getName()+"__"+threadLocal.get());
        });

        executorService.shutdown();
    }
}
//猜猜以上结果是啥？是 哈哈哈 然后  呵呵呵？

//不不不，结果出人意料的是：

//pool-1-thread-1__--->哈哈哈

//pool-1-thread-1__--->哈哈哈
```

是以上代码对 threadLocal 的修改没有成功吗？不，其实修改是成功了，但是由于线程池为了减少线程创建的开支，对使用完毕的线程并没有立即销毁。而是继续返还到了线程池中，所以我们下次使用线程的时候。并不会重新创建一个新的线程，也就是不会执行线程初始化的init方法。也有同学会问。之前初始化InheritableThreadLocal的时候不是引用传递吗？为什么修改不了？因为你没有详细看ThreadLocalMap的创建过程源码。ThreadLocalMap只是复制了一份新的数据，并没有直接使用父线程的InheritableThreadLocal





## 8.java对象锁和类锁的区别

https://www.cnblogs.com/owenma/p/8609348.html

## 9.Synchronized底层原理

说根本的就是创建一个monitor对象:

```java
class SynchronizedDemo {

    public synchronized void m() {
        System.out.printf("WUST");
    }

    public void m2() {
        synchronized(this) {
            System.out.printf("WUST");
        }
    }
}
```

反编译后:

![img](https://upload-images.jianshu.io/upload_images/8415301-765ba6994486af15.png?imageMogr2/auto-orient/strip|imageView2/2/w/437/format/webp)

```
synchronized修饰的方法在字节码中添加了一个ACC_SYNCHRONIZED的flags，同步代码块则是在同步代码块前插入monitorenter，在同步代码块结束后插入monitorexit。这两者的处理是分别是这样的：当线程执行到某个方法时，JVM会去检查该方法的ACC_SYNCHRONIZED访问标志是否被设置，如果设置了那线程会去获取这个对象所对应的monitor对象（每一个对象都有且仅有一个与之对应的monitor对象）,获取成功后才执行方法体，方法执行完再释放monitor对象，在这一期间，任何其他线程都无法获得这个monitor对象。而线程执行同步代码块时遇到的monitorenter和monitorexit指令依赖monitor对象完成。这两者实现的方式本质上无区别，只是方法的同步是一种隐式的方式，不通过字节码实现

注:，第一个monitorexit指令是同步代码块正常释放锁的一个标志；
如果同步代码块中出现Exception或者Error，则会调用第二个monitorexit指令来保证释放锁
```

## 10.线程池捕获异常例子

```java
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class TestBuild {
    static class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            e.printStackTrace();
        }
    }
    static class HandlerThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(@NotNull Runnable r) {
            Thread t = new Thread(r);
            t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
            return t;
        }
    }
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool(new HandlerThreadFactory());
        service.execute(() -> {
            System.out.println("run " + Thread.currentThread().getName());
            throw new RuntimeException();
        });
        service.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("runrun " + Thread.currentThread().getName());
        });
        service.shutdown();
        while (!service.awaitTermination(5, TimeUnit.SECONDS)) {
            Thread.yield();
        }
    }
}
```

## 11.CLH队列

CLH队列中的结点QNode中含有一个locked字段，该字段若为true表示该线程需要获取锁，且不释放锁，为false表示线程释放了锁。结点之间是通过隐形的链表相连，之所以叫隐形的链表是因为这些结点之间没有明显的next指针，而是通过myPred所指向的结点的变化情况来影响myNode的行为。CLHLock上还有一个尾指针，始终指向队列的最后一个结点
当一个线程需要获取锁时，会创建一个新的QNode，将其中的locked设置为true表示需要获取锁，然后线程对tail域调用getAndSet方法，使自己成为队列的尾部，同时获取一个指向其前趋的引用myPred,然后该线程就在前趋结点的locked字段上旋转，直到前趋结点释放锁。当一个线程需要释放锁时，将当前结点的locked域设置为false，同时回收前趋结点。如下图所示，线程A需要获取锁，其myNode域为true，些时tail指向线程A的结点，然后线程B也加入到线程A后面，tail指向线程B的结点。然后线程A和B都在它的myPred域上旋转，一量它的myPred结点的locked字段变为false，它就可以获取锁扫行。明显线程A的myPred locked域为false，此时线程A获取到了锁

```
package com.example.demo.pojo;

import java.util.concurrent.atomic.AtomicReference;

class Node {
    private volatile boolean locked;

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}

public class ClhQueue {
    //队尾
    AtomicReference<Node> tail;
    //前驱
    ThreadLocal<Node> predNode;
    //当前结点
    ThreadLocal<Node> curNode;
    public ClhQueue() {
        tail = new AtomicReference<>(new Node());
        predNode = new ThreadLocal<>(){
            @Override
            protected Node initialValue() {
                return null;
            }
        };
        curNode = new ThreadLocal<>() {
            @Override
            protected Node initialValue() {
                return new Node();
            }
        };
    }
    public void acquired() {
        //获取当前线程的自己的节点,第一次会自动初始化
        Node cur = curNode.get();
        //插入队尾
        System.out.println(Thread.currentThread().getName() + "插入队尾");
        Node pred = tail.getAndSet(cur);
        predNode.set(pred);
        while (!pred.isLocked()) {
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName() + "获取锁");
    }

    public void release() {
        Node cur = curNode.get();
        cur.setLocked(false);
        //将当前的节点移除队列
        curNode.set(predNode.get());
    }
}
```

## 12.Jdk1.6后锁的优化

- **jdk1.6前重量级锁Synchronized的弊端**

  java的线程是映射到操作系统原生线程上的,如果要阻塞或唤醒一个线程需要操作系统介入,需要在用户态和核心态之间切换,这种切换回消耗大量系统资源,因为用户态和内核态有各自专用内存空间,专用寄存器,用户态切换到内核态需要传递很多变量和参数,内核也要保护用户态在切换时的一些寄存器值,变量,以便内核态调用结束后切换用户态继续工作

- 自旋锁

  如果持有锁线程能在很短时间释放锁资源,等待竞争锁的线程就不需要做内核态和用户态直接的切换进入阻塞挂起状态,只需要重复执行获取锁动作即可,说白就是让cpu做无用功

## 13.ForkjoinPool原理

https://blog.csdn.net/qq_34707456/article/details/106695302

## 14.CountDownLauch底层原理

1.基于Aqs框架,内部声明静态内部类Syns继承AbstractQueuedSynchronizer,重写了tryAcquireShared和tryReleaseShared方法

![image-20210610004447052](C:\Users\hasee\AppData\Roaming\Typora\typora-user-images\image-20210610004447052.png)

2.两个主要方法await和countDown

await调用Aqs的acquireSharedInterruptibly自带方法（可中断,向上抛出中断异常）,内部调用tryAcquireShared和doAcquireSharedInterruptibly方法

![image-20210610005257909](C:\Users\hasee\AppData\Roaming\Typora\typora-user-images\image-20210610005257909.png)

doAcquireSharedInterruptibly的实现采用的是AQSCLH隐式双向链表队列,每个Node维护着一个Thread对象,首先将当前Node插入队尾，查看自己优先权,如果前面不是队列头部,并且当前state值是否为0,如果是则唤醒后面节点(每个Node有个next指向下个,调用LockSupport.unpark(next.thread))去唤醒,否则的话执行LockSupport.park(this)阻塞当前线程

countDown的方法会调用releaseShared将当前的state值进行减1操作,state减到0

![image-20210610010338137](C:\Users\hasee\AppData\Roaming\Typora\typora-user-images\image-20210610010338137.png)

## 15.ThreadPool底层原理

- 线程池的好处:

实现线程的复用,Java1.5中引入的Executor框架把任务的提交和执行进行解耦，只需要定义好任务，然后提交给线程池，而不用关心该任务是如何执行、被哪个线程执行，以及什么时候执行。

- 线程池各个参数介绍

  - corePoolSize

    ```
    线程池中的核心线程数,当提交一个任务时,线程池创建一个新线程执行任务,直到当前线程数等于corePoolSize,如果当前线程数为corePoolSize,继续提交的任务被保存到阻塞队列中,等待被执行,如果执行了线程池ThreadPoolExecutor的prestartAllCoreThreads()方法,线程池会提前创建并启动所有核心线程
        /**
         * Starts all core threads, causing them to idly wait for work. This
         * overrides the default policy of starting core threads only when
         * new tasks are executed.
         *
         * @return the number of threads started
         */
        public int prestartAllCoreThreads() {
            int n = 0;
            while (addWorker(null, true))
                ++n;
            return n;
        }
    ```

  - maximunPoolSize

    ```
    线程池中允许的最大线程数,如果当前阻塞队列满了,且继续提交任务,则创建新的线程执行任务,前提是当前线程数小于maximunPoolSize
    ```

  - keepAliveTime

    ```
    线程空闲时的存活时间,即当线程没有任务执行时,继续存活的时间;默认的情况,这参数只在线程数大于corePoolSize时才有用
    ```

  - unit

    ```
    keepAliveTime的单位
    ```

  - workQueue

    ```
    用来保存等待被执行的任务阻塞队列,且任务必须实现Runnable接口,在JDK中提供如下阻塞队列
    1.ArrayBlockingQueue:基于数据的有界阻塞队列,按FIFO排序任务
    2.LinkedBlockingQueue:基于链表结构的阻塞队列,按FIFO排序任务,吞吐量通常要高于ArrayBlockingQueue
    3.SynChronousQueue:一个不存储元素的阻塞队列,每个插入插入操作必须等到另一个线程调用移除操作,否则插入操作一直处于阻塞状态,吞吐量通常要高于LinkedBlockingQueue
    4.priorityBlockingQueue:具有优先级的无界阻塞队列
    ```

  - threadFactory

    ```
    创建线程的工厂,通过自定义的线程工厂可以给每个新建的线程设置一个具有识别度的线程名
    ```

    ![img](https://upload-images.jianshu.io/upload_images/2184951-d2d8fd007c7f7a27.png?imageMogr2/auto-orient/strip|imageView2/2/w/640/format/webp)

  - handler

    ```
    线程池的饱和策略，当阻塞队列满了，且没有空闲的工作线程，如果继续提交任务，必须采取一种策略处理该任务，线程池提供了4种策略：
    1、AbortPolicy：直接抛出异常，默认策略；
    2、CallerRunsPolicy：用调用者所在的线程来执行任务；
    3、DiscardOldestPolicy：丢弃阻塞队列中靠最前的任务，并执行当前任务；
    4、DiscardPolicy：直接丢弃任务；
    当然也可以根据应用场景实现RejectedExecutionHandler接口，自定义饱和策略，如记录日志或持久化存储不能处理的任务
    ```

    

  - Executors

    Exectors工厂类提供了线程池的初始化接口，主要有如下几种：

    - newFixedThreadPool

      ![img](https://upload-images.jianshu.io/upload_images/2184951-14823f17e4e4a09e.png?imageMogr2/auto-orient/strip|imageView2/2/w/772/format/webp)

      ```
      初始化一个指定线程数的线程池，其中corePoolSize == maximumPoolSize，使用LinkedBlockingQuene作为阻塞队列，不过当线程池没有可执行任务时，也不会释放线程。
      ```

    - newCachedThreadPool

      ![img](https://upload-images.jianshu.io/upload_images/2184951-9b76630ac48f318c.png?imageMogr2/auto-orient/strip|imageView2/2/w/657/format/webp)

      ```
      1、初始化一个可以缓存线程的线程池，默认缓存60s，线程池的线程数可达到Integer.MAX_VALUE，即2147483647，内部使用SynchronousQueue作为阻塞队列
      2、和newFixedThreadPool创建的线程池不同，newCachedThreadPool在没有任务执行时，当线程的空闲时间超过keepAliveTime，会自动释放线程资源，当提交新任务时，如果没有空闲线程，则创建新线程执行任务，会导致一定的系统开销；
      所以，使用该线程池时，一定要注意控制并发的任务数，否则创建大量的线程可能导致严重的性能问题。
      ```

    - newSingleThreadExecutor

      ![img](https://upload-images.jianshu.io/upload_images/2184951-deded05302aaf255.png?imageMogr2/auto-orient/strip|imageView2/2/w/796/format/webp)

      ```
      初始化的线程池中只有一个线程，如果该线程异常结束，会重新创建一个新的线程继续执行任务，唯一的线程可以保证所提交任务的顺序执行，内部使用LinkedBlockingQueue作为阻塞队列。
      ```

    - newScheduledThreadPool

      ![img](https://upload-images.jianshu.io/upload_images/2184951-14823f17e4e4a09e.png?imageMogr2/auto-orient/strip|imageView2/2/w/772/format/webp)

      ```
      初始化的线程池可以在指定的时间内周期性的执行所提交的任务，在实际的业务场景中可以使用该线程池定期的同步数据
      ```

    

  - 线程池源码剖析

    除了newScheduledThreadPool,其他线程池都基于ThreadPoolExecutor类实现

    - 线程池内部的状态

      ![img](https://upload-images.jianshu.io/upload_images/2184951-5a620e0f56cbb008.png?imageMogr2/auto-orient/strip|imageView2/2/w/684/format/webp)

      ```
      1.AtomicInteger是一个控制可信,高3位表示线程池的运行状态,低29位表示线程池中的线程数
      2.RUNNING:-1 <<COUNT_BITS，结果就是
      ```

      

    - 

    - 

    - 

    - 

    - 

    - 

    - 

    



# 四.Jvm知识

## 1.内存泄漏和内存溢出的区别

```
内存泄漏:
存在一些被分配的对象,这些对象是有引用的,但是程序以后不会再使用这些对象,满足这两个条件就是内存泄漏,这些对象不会被GC回收,然而它却它却占用内存
内存溢出:
系统中存在无法回收的对象或使用的内存过多，最终使得程序运行要用到的内存大于能提供的最大内存
```

## 2.导致Java内存泄漏的因素

```java
1、静态集合类引起内存泄漏
(声明为static)的HashMap、Vector等的使用最容易出现内存泄露，这些静态变量的生命周期和应用程序一致，他们所引用的所有的对象Object也不能被释放，因为他们也将一直被Vector等引用着。
2、当集合里面的对象(重写了hashCode方法)属性被修改后，再调用remove()方法时不起作用。
3、监听器
在释放对象的时候却没有去删除这些监听器，增加了内存泄漏的机会。
4、各种连接
比如数据库连接(dataSourse.getConnection())，网络连接(socket)和io连接，除非其显式的调用了其close()方法将其连接关闭，否则是不会自动被GC 回收的。
5、内部类和外部模块的引用
内部类的引用是比较容易遗忘的一种，而且一旦没释放可能导致一系列的后继类对象没有释放。此外程序员还要小心外部模块不经意的引用，例如程序员A 负责A 模块，调用了B 模块的一个方法如： public void registerMsg(Object b); 这种调用就要非常小心了，传入了一个对象，很可能模块B就保持了对该对象的引用，这时候就需要注意模块B 是否提供相应的操作去除引用。
6、单例模式
不正确使用单例模式是引起内存泄漏的一个常见问题，单例对象在初始化后将在JVM的整个生命周期中存在(以静态变量的方式)，如果单例对象持有外部的引用，那么这个对象将不能被JVM正常回收，导致内存泄漏
```

## 3.栈上分配与TLAB

Java除了堆,还有两块地方可以存放对象,两个地方分别是栈和TLAB.

- 栈上分配

  ```
  如果确定一个对象的作用域不会逃逸出方法之外,可以将这个对象分配在栈上,这样,对象所占用的内存空间就可以随栈帧出栈而销毁.JVM允许将线程私有的对象打散分在栈上,而不是堆上
  ```

  栈上分配技术：

  - 逃逸分析

    逃逸分析的目的是判断对象的作用域是否可能逃逸函数体

  - 标量替换

    允许将对象打散分配在栈上,比如一个对象有两字段,会将这两字段作为局部变量进行分配

  只能在Server模式下才能启用逃逸分析,参数-XX-DoEscapeAnalysis启用逃逸分析,参数XX-EliminateAllocations开启标量替换(默认打开).Java SE6U23版本后,HostSpot中默认开启逃逸分析,可通过选项-XX:PrintEscapeAnalysis查看逃逸分析的筛选结果

- TLAB

  TLAB全称是Thread Local Allocation Buffer,即线程本地分配缓存区,这是一个线程专用的内存分配区域

  对象一般会分配在堆上，堆是全局共享的.因此在同一时间,会有多个线程在堆上申请空间，每次分配都需要进行同步(虚拟机采用cas配上失败重试的方式保证更新操作的原子性),竞争激烈的场合分配效率会比较低,所以jvm使用TLAB避免多线程冲突,在给对象分配内存时,每个线程使用自己的TLAB，这样可以避免线程同步,提高了对象分配效率

  TLAB本身占用eden区空间,开启TLAB情况下,虚拟机会给每个线程分配一块TLAB空间,参数-XX:+UseTLAB开启TLAB,默认是开启的,TLAB空间内存比较小,缺省情况下仅占有整个eden空间的1%,可以通过选项-XX:TLABWasteTargetPercent设置TLAB空间锁占用Eden空间百分比

  TLAB空间一般不会太大,因此大对象无法在TLAB上进行分配,会先分配在堆上.TLAB空间由于比较小,很容易装满,虚拟机内部维护一个叫refill_waste的值,当请求对象大于refill_waste时,会选择在堆中分配,若小于该值,则会废除当前TLAB,会废除当前TLAB，新建TLAB来分配对象,这个阈值可以使用TLABRefillWasteFraction来调整,它表示TLAB允许产生这种浪费的比例,默认为64，就是约1/64的TLAB空间作为refill_waste，默认情况下,TLAB和refill_waste会在运行时不断调整,使系统运行状态达到最优.如果要禁用自动调整TLAB大小,使用-XX-RrsizeTLAB禁用

​      每个线程都会从eden区分配一块空间,作为自己的TLAB,start是起始地址,end是末尾地址,top是当前的分配指针

 假设TLAB大小为100KB，refill_waste（可允许浪费空间的值）为5KB
　　1、假如当前TLAB已经分配96KB，还剩下4KB，但是现在new了一个对象需要6KB的空间，显然TLAB的内存不够了，这时可以简单的重新申请一个TLAB，原先的TLAB交给Eden管理，这时只浪费4KB的空间，在refill_waste 之内。
　　2、假如当前TLAB已经分配90KB，还剩下10KB，现在new了一个对象需要11KB，显然TLAB的内存不够了，这时就不能简单的抛弃当前TLAB，因为此时抛弃的话，就会浪费10KB的空间，10KB是大于咱们设置的refill_waste（可允许浪费空间的值）5KB的，所以此时会保留当前的TLAB不动，会把这11KB会被安排到Eden区进行申请。

![640?wx_fmt=png](https://ss.csdn.net/p?https://mmbiz.qpic.cn/mmbiz_png/VwA5cCq3BlDVeta0Usb70mjcvCS8jJO3xKgbJWXH4BprsdjthvakAkuC6544IsJicjCibXNkS5ibLz253Fa0oLIibw/640?wx_fmt=png)

- 对象内存分配的两种方法

  - 指针碰撞(Serail,ParNew等待Compact过程的收集器)

    假设堆中内存绝对规整,所有用过内存都放一边,空闲内存放另一边,中间放一个指针作为分界点,分配内存仅仅把指针向空闲空间那边挪动一段与对象大小相等的距离

  - 空闲列表(CMS基于Mark-Sweep算法的收集器)

    如果堆中内存并不是规整的,已使用的内存和空闲的内存相互交错,虚拟机就维护一个列表,记录上哪些内存是可用的,在分配的时候从列表中找到一块足够大的空间分给对象实例,并更新列表上的记录

## 4.垃圾收集算法

(1)标记清楚(Mark-Sweep)算法

先标记所有需要回收的对象,对标记完成后的对象进行统一回收

![img](http://files.jb51.net/file_images/article/201702/2017021415541024.png)

缺点:标记和清除两个过程效率都不高,另一个是空间问题,标记清楚后会产生大量不连续的内存碎片,会导致无法分配大对象

(2)复制算法

针对标记,清楚算法不足,复制算法将可用内存容量分为大小相等两块,每次只用一块,一块内存用完,清除掉可回收的对象,剩余的对象复制到另一块

![img](http://files.jb51.net/file_images/article/201702/2017021415541125.png)

缺点:使用内存比原来缩小一半

(3)标记-整理(Mark-Compact)算法

复制算法一般对对象存活率比较低的一种对象进行回收,对象存活率较高的内存,效果不理想,标记-整理算法一开始对回收对象进行标记,让所有存活对象移到一端,直接清理掉端边界以外内存

![img](http://files.jb51.net/file_images/article/201702/2017021415541226.png)

## 5.垃圾收集器

## 6.如何解决oom，用什么命令

1.使用jmap -heap [线程号] 查看概要

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119163738375.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1cWlhbmxlaQ==,size_16,color_FFFFFF,t_70#pic_center)

2.使用jmap -histo 线程号打印每个class的实例数目,内存占用,类全名信息

jmap -histo:live 6956只统计活的对象数量

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119163800279.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2h1cWlhbmxlaQ==,size_16,color_FFFFFF,t_70#pic_center)

3.导出dump 文件 jmap -dump:format=b,file=D:\test\heap.hprof 6956

4.mat工具分析

https://blog.csdn.net/lililuni/article/details/92602730

## 7.jvm对象的结构以及锁的标志位详解

https://blog.csdn.net/fedorafrog/article/details/113747912

## 8.(JIT)即时编译器C1和C2的区别

先看jvm运行的原理

![img](https://img-blog.csdn.net/20160812104144969?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

对于大部分开发者,java编译器指jdk自带的javac指令,该指令可将java源程序编译成.class文件,包含的代码格式称为java字节码.

这种格式代码无法直接在操作系统运行,需要经过不同平台jvm的解释器走解释执行,这种效率比较低,jvm中即时编译器(JIT compiller)会在运行时有选择性将运行次数较多的热点代码编译成二进制代码,直接运行在底层硬件

**热点代码的理解:**

- 1.被多次调用的方法

- 2.被多次执行的循环体

  两种情况都是以整个方法作为编译对象,这种编译方法因为编译发生在方法执行过程之中,所以被称为**栈上替换**,即方法栈帧还在栈上,方法就被替换了

**如何判断是否是热点代码:**热点探测

**(1)基于采样的热点探测**

采用这种方法的虚拟机会周期性检查各个线程的栈顶,如果发现某些方法经常出现在栈顶,那这个方法就是热点方法，这探测方法的好处是实现简单高效,还可以很容易获取方法调用关系(将调用堆栈展开即可),缺点是很难精确确认一个方法热度,容易受到线程阻塞或其他因素影响扰乱热点探测

**(2)基于计数器的热点探测**

对每个方法建立计数器，统计方法执行次数,如果执行次数超过一定阈值,就认为是热点代码,，这是Hotspot虚拟机默认使用的

计数器类型:

- 方法调用计数器

  顾名思义，这个计数器用于统计方法被调用的次数。
  当一个方法被调用时，会先检查该方法是否存在被JIT编译过的版本，如果存在，则优先使用编译后的本地代码来执行。如果不存在已被编译过的版本，则将此方法的调用计数器值加1，然后判断方法调用计数器与回边计数器值之和是否超过方法调用计数器的阈值。如果超过阈值，那么将会向即时编译器提交一个该方法的代码编译请求。
  如果不做任何设置，执行引擎并不会同步等待编译请求完成，而是继续进行解释器按照解释方式执行字节码，直到提交的请求被编译器编译完成。当编译工作完成之后，这个方法的调用入口地址就会系统自动改写成新的，下一次调用该方法时就会使用已编译的版本

  ![img](https://img-blog.csdn.net/20160812101630575?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

- 回边计数器

  它的作用就是统计一个方法中*循环体*代码执行的次数，在字节码中遇到控制流向后跳转的指令称为“回边”。

  ![img](https://img-blog.csdn.net/20160812102239062?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)



即时编译器有两类(C1编译器和C2编译器,),分别用在客户端和服务端,主流的HotSpot虚拟机中默认采用解释器和其中一个编译器直接配合,程序使用哪个编译器,取决于虚拟机运行模式,可以使用-client或-server强制指定虚拟机运行在哪个模式,jdk1.7后默认使用分层编译,C1和C2同时运行

C1可以获取更高的编译速度,C2可获取更好的编译质量,C1关注点在于局部的优化,放弃更多耗时的全局优化,C2面向全局的优化

C1和C2的优化策略:

- C1主要与方法内联,去虚拟化,冗余消除

  - 方法内联:将引用函数代码编译到引用点处,减少栈帧产生,减少参数传递和跳转过程
  - 去虚拟化:对唯一的实现类进行内联
  - 冗余消除:运行期间，将一些不会运行的代码折叠掉

- C2的优化主要在全局层面,逃逸分析是优化的基础,基于逃逸分析在C2上有如下几种优化

  - 标量替换:用标量值替换聚合对象的属性值

    ```
    通过逃逸分析确定该对象不会被外部访问，并且对象可以被进一步分解时，JVM不会创建该对象，而会将该对象成员变量分解若干个被这个方法使用的成员变量所代替。这些代替的成员变量在栈帧或寄存器上分配空间。
    通过-XX:+EliminateAllocations可以开启标量替换， -XX:+PrintEliminateAllocations查看标量替换情况（Server VM 非Product版本支持）
    ```

    

  - 栈上分配:用于未逃逸的对象分配在栈而不是堆

    ```
    我们通过JVM内存分配可以知道JAVA中的对象都是在堆上进行分配，当对象没有被引用的时候，需要依靠GC进行回收内存，如果对象数量较多的时候，会给GC带来较大压力，也间接影响了应用的性能。为了减少临时对象在堆内分配的数量，JVM通过逃逸分析确定该对象不会被外部访问。那就通过标量替换将该对象分解在栈上分配内存，这样该对象所占用的内存空间就可以随栈帧出栈而销毁，就减轻了垃圾回收的压力。
    通过-XX:-DoEscapeAnalysis关闭逃逸分析
    ```

    

  - 同步消除:清除同步操作,通常指synchronized

    ```
    如果你定义的类的方法上有同步锁，但在运行时，却只有一个线程在访问，此时逃逸分析后的机器码，会去掉同步锁运行，这就是没有出现线程逃逸的情况。那该对象的读写就不会存在资源的竞争，不存在资源的竞争，则可以消除对该对象的同步锁
    通过-XX:+EliminateLocks可以开启同步消除,进行测试执行的效率
    ```

    

Jdk1.7后默认开启分层编译策略:

程序解释执行(不开启性能监控)可以触发C1编译,将字节码编译为机器码,可以进行简单优化,也可以加上性能监控,C2编译会根据性能监控信息进行激进优化

当显示指定-server,默认开启分层编译策略,由C1编译器和C2编译器相互协作共同执行编译任务

https://www.cnblogs.com/zyf-yxm/p/13661980.html

https://www.cnblogs.com/insistence/p/5901457.html



## 9.jvm的垃圾回收器的类型

https://blog.csdn.net/weixin_41910694/article/details/90767861

- **如何查看服务器默认垃圾回收器**

  查看默认的垃圾收集器，JVM参数：
  java -XX:+PrintCommandLineFlags -version

  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190604135539561.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTkxMDY5NA==,size_16,color_FFFFFF,t_70)

### 1.串行垃圾回收器(Serail Garbage Collector)

(1)串行垃圾回收器在进行垃圾回收时,会持有所有应用程序的线程,冻结所有应用程序线程，单个垃圾回收线程来进行来进行垃圾回收工作

(串行)收集器是最古老,最稳定以及效率高的收集器,可能会产生较长停顿,只使用一个线程回收.新生代,老年代使用串行回收,新生代复制算法,老年代标记-压缩,垃圾收集过程会stop the world

使用方式:-XX:+UseSerialGC 串联收集,在jdk client 模式,不指定vm参数，默认是串行垃圾回收

### 2.串行ParNer收集器

ParNew 收集器其实就是Serial收集器的多线程版本。新生代并行,老年代串行;新生代复制算法,老年代标记-压缩

使用方法:-XX:+UserParNewGC ParNew收集器

​               -XX:+ParallelGCThreads 限制线程数量

### 3.并行:Parallel收集器

Parallel Scavenge收集器类似ParNew收集器,Parallel收集器更关注系统的吞吐量.可以通过参数来打开自适应调节策略,虚拟机会根据当前系统的运行情况收集性能监控信息,动态调整这些参数以提供最合适的停顿时间或最大吞吐量;也可以通过参数控制GC的实际不大于多少毫秒或者比例;新生代复制算法,老年代标记-压缩

使用方法:-XX:+UseParallelGC 使用Parallel收集器+老年代串行

### 4.并行:Parallel Old收集器

Parallel Old是Parallel scavenge收集器的老年代版本,使用多线程和标记-整理算法.这个收集器是在JDK1.6才开始提供

使用方法:-XX:UseParallelOldGC使用Parallel收集器+老年代并行

### 5.并发标记扫描cms收集器

CMS收集器是一种以获取最短回收停顿时间为目标的收集器,目前很大一部分java应用集中在互联网站或b/s系统的服务端上,这类应用尤其重视服务的响应速度,希望系统停顿时间最短,以给用户带来较好的体验,它基于标记-清除算法实现,整个回收过程分为4步

```
1.初始标记
2.并发标记
3.重新标记
4.并发清除
```

其中初始标记,重新标记这两步骤仍然要stop the world，出师表及仅仅是标记一下GC Roots能直接关联到的对象,速度很快,并发标记阶段就是进行GC Roots tracing的过程,而重新标记则是为了修正并发标记期间,因用户程序持续运作而导致标记产生变动的那一部分对象的标记记录,这个阶段停顿时间一般比初始标记阶段稍长一些,但远比并发标记时间短.整个过程耗时最长的并发标记和并发清除中,收集器线程可以和用户线程一起工作,所以总体上,cmd收集器的内存回收过程与用户线程一起并发

优点:并发收集、低停顿
 缺点：产生大量空间碎片、并发阶段会降低吞吐量

-XX:+UseConcMarkSweepGC 使用CMS收集器

-XX:+ UseCMSCompactAtFullCollection Full GC后，进行一次碎片整理；整理过程是独占的，会引起停顿时间变长
-XX:+CMSFullGCsBeforeCompaction 设置进行几次Full GC后，进行一次碎片整理
-XX:ParallelCMSThreads 设定CMS的线程数量（一般情况约等于可用CPU数量） 

## 10.jvm堆中对象头的内部结构及锁状态

https://blog.csdn.net/qq_43012792/article/details/107405527

# 五.微服务框架知识

## 1.springboot启动的两种方式

(1)在controller中添加@EnableAutoConfiguration

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190602234356727.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L09ubHlvbmVGcmlzdA==,size_16,color_FFFFFF,t_70)

(2)将所有的Controller集中放置在一个类中：必须要添加一个包扫描@ComponentScan的注释：

![在这里插入图片描述](https://www.pianshen.com/images/858/b7994df3829f978e64a8b35d1f061322.png)

（3）使用@SpringbootApplication注解

![image-20210415232926502](C:\Users\hasee\AppData\Roaming\Typora\typora-user-images\image-20210415232926502.png)

## 2.springboot如果在启动前需要引入自己代码,要继承哪个接口

- 实现ApplicationRunner接口

  ```
  @Component
  public class RunUtilTest  implements ApplicationRunner {
      @Override
      public void run(ApplicationArguments args) throws Exception {
          System.out.println(1111);
  
      }
  }
  ```

- 实现CommandLineRunner接口

  ```
  @Component
  public class RunUtilTest2 implements CommandLineRunner {
  
      @Override
      public void run(String... args) throws Exception {
          System.out.println(2133254);
      }
  }
  ```

  

- 如果要排序执行,添加@Order注解

  - 加在类上，注解的value值越小，越先执行

  - 例如下面代码就是RunUtilTest2先执行

    ```
    @Component
    @Order(value=1)
    public class RunUtilTest2 implements CommandLineRunner {
    
        @Override
        public void run(String... args) throws Exception {
            System.out.println(2133254);
        }
    }
    
    @Component
    @Order(value=2)
    public class RunUtilTest  implements ApplicationRunner {
        @Override
        public void run(ApplicationArguments args) throws Exception {
            System.out.println(1111);
    
        }
    }
    ```

    

## 3.如何保证接口的幂等性

https://blog.csdn.net/qq_42005257/article/details/95616179

## 4.如何设计一个秒杀系统

## 5.@Configuration和@Component的区别

```java
@Configuration
public class MyTestConfig {

    @Bean
    public Driver driver(){
        Driver driver = new Driver();
        driver.setId(1);
        driver.setName("driver");
        driver.setCar(car());
        return driver;
    }

    @Bean
    public Car car(){
        Car car = new Car();
        car.setId(1);
        car.setName("car");
        return car;
    }
}

测试代码如下

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestApplicationTests {

    @Autowired
    private Car car;

    @Autowired
    private Driver driver;

    @Test
    public void contextLoads() {
        boolean result = driver.getCar() == car;
        System.out.println(result ? "同一个car" : "不同的car");
    }

}

打印结果如下：
同一个car
```

从上面的结果可以发现使用Configuration时在driver和spring容器之中的是同一个对象，而使用Component时是不同的对象。
造成不同结果的原因在ConfigurationClassPostProcessor类之中，通过调用enhanceConfigurationClasses方法，为被注解@Configuration的类进行CGLIB代理，代码如下：

```
public void enhanceConfigurationClasses(ConfigurableListableBeanFactory beanFactory) {
        Map<String, AbstractBeanDefinition> configBeanDefs = new LinkedHashMap<String, AbstractBeanDefinition>();
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDef = beanFactory.getBeanDefinition(beanName);
            if (ConfigurationClassUtils.isFullConfigurationClass(beanDef)) {//判断是否被@Configuration标注
                if (!(beanDef instanceof AbstractBeanDefinition)) {
                    throw new BeanDefinitionStoreException("Cannot enhance @Configuration bean definition '" +
                            beanName + "' since it is not stored in an AbstractBeanDefinition subclass");
                }
                else if (logger.isWarnEnabled() && beanFactory.containsSingleton(beanName)) {
                    logger.warn("Cannot enhance @Configuration bean definition '" + beanName +
                            "' since its singleton instance has been created too early. The typical cause " +
                            "is a non-static @Bean method with a BeanDefinitionRegistryPostProcessor " +
                            "return type: Consider declaring such methods as 'static'.");
                }
                configBeanDefs.put(beanName, (AbstractBeanDefinition) beanDef);
            }
        }
        if (configBeanDefs.isEmpty()) {
            // nothing to enhance -> return immediately
            return;
        }
        ConfigurationClassEnhancer enhancer = new ConfigurationClassEnhancer();
        for (Map.Entry<String, AbstractBeanDefinition> entry : configBeanDefs.entrySet()) {
            AbstractBeanDefinition beanDef = entry.getValue();
            // If a @Configuration class gets proxied, always proxy the target class
            beanDef.setAttribute(AutoProxyUtils.PRESERVE_TARGET_CLASS_ATTRIBUTE, Boolean.TRUE);
            try {
                // Set enhanced subclass of the user-specified bean class
                Class<?> configClass = beanDef.resolveBeanClass(this.beanClassLoader);
                Class<?> enhancedClass = enhancer.enhance(configClass, this.beanClassLoader);//生成代理的class
                if (configClass != enhancedClass) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Replacing bean definition '%s' existing class '%s' with " +
                                "enhanced class '%s'", entry.getKey(), configClass.getName(), enhancedClass.getName()));
                    }
                    //替换class，将原来的替换为CGLIB代理的class
                    beanDef.setBeanClass(enhancedClass);
                }
            }
            catch (Throwable ex) {
                throw new IllegalStateException("Cannot load configuration class: " + beanDef.getBeanClassName(), ex);
            }
        }
    }
```

从上面可以看到，虽然Component注解也会当做配置类，但是并不会为其生成CGLIB代理Class，所以在生成Driver对象时和生成Car对象时调用car()方法执行了两次new操作，所以是不同的对象。当时Configuration注解时，生成当前对象的子类Class，并对方法拦截，第二次调用car()方法时直接从BeanFactory之中获取对象，所以得到的是同一个对象

## 6.Springboot和springMvc,SpringCloud的区别和关系

```
Springboot是一个快速开发框架,能快速整个第三方框架,内嵌tomcat,采用注解的形式,springmvc是springboot使用的web组件,是一个控制层,而SpringCLoud依赖SpringBoot组件,使用SpringMVC编写Http协议接口,并且SpringCloud是一套完整的微服务解决框架
spring-boot-starter-web整合SpringMVC,Spring
```

springmvc有如下功能:

(1)实现RPC远程通讯技术

(2)帮助springboot编写http+json协议的接口

## 7.springboot的启动过程

https://www.cnblogs.com/trgl/p/7353782.html

## 8.spring的启动过程

https://www.cnblogs.com/wyq178/p/11415877.html

## 9.Spring如何解决循环依赖

两种方案,一种是用setter注入,一种使用@Lazy注解或lazy-init标签。Setter注入用的原理是用的Spring的三级缓存,懒加载用的原理是使用到该对象才去加载

- Setter注入原理

  ![img](https://img-blog.csdn.net/20180331212327518?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM2MzgxODU1/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

  由图可知,spring先将Bean对象实例化，再设置对象属性,实例化结束将对象放到一个Map中,并提供获取这个未设置属性的实例化对象的引用方法.

  Spring单例对象初始化分为三步

  ![img](https://img-blog.csdn.net/20180330100217757?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM2MzgxODU1/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

  1.createBeanInstance:实例化,也就是调用对象构造方法实例化对象

  2.populateBean:填充属性,主要是多bean的依赖属性进行填充

  3.initializeBean:调用spring xml中的init()方法

循环依赖主要发生在第一第二步，也就是构造器循环依赖和属性循环依赖,Spring为了解决单例的循环依赖使用三级缓存:

-  singletonFactories ： 单例对象工厂的cache 

-  earlySingletonObjects ：提前暴光的单例对象的Cache 。【用于检测循环引用，与singletonFactories互斥】

-  singletonObjects：单例对象的cache

我们在创建bean的时候，首先想到的是从cache中获取这个单例的bean，这个缓存就是singletonObjects。主要调用方法就就是

```java
protected Object getSingleton(String beanName, boolean allowEarlyReference) {
    Object singletonObject = this.singletonObjects.get(beanName);
    if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
        synchronized (this.singletonObjects) {
            singletonObject = this.earlySingletonObjects.get(beanName);
            if (singletonObject == null && allowEarlyReference) {
                ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();
                    this.earlySingletonObjects.put(beanName, singletonObject);
                    this.singletonFactories.remove(beanName);
                }
            }
        }
    }
    return (singletonObject != NULL_OBJECT ? singletonObject : null);
}
```

上面的代码需要解释两个参数：

- isSingletonCurrentlyInCreation()判断当前单例bean是否正在创建中，也就是没有初始化完成(比如A的构造器依赖了B对象所以得先去创建B对象， 或则在A的populateBean过程中依赖了B对象，得先去创建B对象，这时的A就是处于创建中的状态。)
- allowEarlyReference 是否允许从singletonFactories中通过getObject拿到对象

分析getSingleton()的整个过程，Spring首先从一级缓存singletonObjects中获取。如果获取不到，并且对象正在创建中，就再从二级缓存earlySingletonObjects中获取。如果还是获取不到且允许singletonFactories通过getObject()获取，就从三级缓存singletonFactory.getObject()(三级缓存)获取，如果获取到了则：

```
this.earlySingletonObjects.put(beanName, singletonObject);
                        this.singletonFactories.remove(beanName);
```

从singletonFactories中移除，并放入earlySingletonObjects中。其实也就是从三级缓存移动到了二级缓存。

从上面三级缓存的分析，我们可以知道，Spring解决循环依赖的诀窍就在于singletonFactories这个三级cache。这个cache的类型是ObjectFactory，定义如下：

```
public interface ObjectFactory<T> {
    T getObject() throws BeansException;
}
```

这个接口在下面被引用

```

protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
    Assert.notNull(singletonFactory, "Singleton factory must not be null");
    synchronized (this.singletonObjects) {
        if (!this.singletonObjects.containsKey(beanName)) {
            this.singletonFactories.put(beanName, singletonFactory);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.add(beanName);
        }
    }
}
```

这里就是解决循环依赖的关键，这段代码发生在createBeanInstance之后，也就是说单例对象此时已经被创建出来(调用了构造器)。这个对象已经被生产出来了，虽然还不完美（还没有进行初始化的第二步和第三步），但是已经能被人认出来了（根据对象引用能定位到堆中的对象），所以Spring此时将这个对象提前曝光出来让大家认识，让大家使用。
这样做有什么好处呢？让我们来分析一下“A的某个field或者setter依赖了B的实例对象，同时B的某个field或者setter依赖了A的实例对象”这种循环依赖的情况。A首先完成了初始化的第一步，并且将自己提前曝光到singletonFactories中，此时进行初始化的第二步，发现自己依赖对象B，此时就尝试去get(B)，发现B还没有被create，所以走create流程，B在初始化第一步的时候发现自己依赖了对象A，于是尝试get(A)，尝试一级缓存singletonObjects(肯定没有，因为A还没初始化完全)，尝试二级缓存earlySingletonObjects（也没有），尝试三级缓存singletonFactories，由于A通过ObjectFactory将自己提前曝光了，所以B能够通过ObjectFactory.getObject拿到A对象(虽然A还没有初始化完全，但是总比没有好呀)，B拿到A对象后顺利完成了初始化阶段1、2、3，完全初始化之后将自己放入到一级缓存singletonObjects中。此时返回A中，A此时能拿到B的对象顺利完成自己的初始化阶段2、3，最终A也完成了初始化，进去了一级缓存singletonObjects中，而且更加幸运的是，由于B拿到了A的对象引用，所以B现在hold住的A对象完成了初始化。

知道了这个原理时候，肯定就知道为啥Spring不能解决“A的构造方法中依赖了B的实例对象，同时B的构造方法中依赖了A的实例对象”这类问题了！因为加入singletonFactories三级缓存的前提是执行了构造器，所以构造器的循环依赖没法解决

## 10.构造注入和set注入的区别

1.构造注入是强聚合关系,先要初始化构造函数中的实例对象才能初始化自己,Set注入先初始化自己再进行其他对象的注入

2.构造函数里对像太多时，效率会更低,必须传入所有参数才能初始化,而setter注入能选择

## 11.spring中bean创建过程/Bean的生命周期

https://www.cnblogs.com/kenshinobiy/p/4652008.html

## 12.Springboot的监视器

此功能用于控制spring boot程序和查看程序信息

https://www.cnblogs.com/ye-hcj/p/9614109.html

首先是配置依赖

```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
	<version>2.0.4.RELEASE</version>
</dependency>
```

properties增加监视器的端口和其他配置

```
server.tomcat.uri-encoding=UTF-8
# 程序运行端口
server.port=8888
# 监视程序运行端口
management.server.port=8090
# 激活所有的内置Endpoints
management.endpoints.web.exposure.include=*
# 开启shutdown这个endpoint
management.endpoint.shutdown.enabled=true
```

开启后通过http://localhost:8090/actuator/env等可以访问,以下是常用的

```
actuator/health         查看程序健康信息
actuator/metrics        查看监视标准
actuator/beans          列出程序中的Spring BEAN 
actuator/env            列出程序运行所有信息
```

- **书写监视控制器**

```
package com.springlearn.learn.Controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController{

    @ResponseBody
    @RequestMapping("/GetEndPoints")
    public String GetAllPoint(HttpServletRequest request){
        String path = request.getContextPath();
        String host = request.getServerName();
        String endPointPath = "/actuator";
        StringBuilder sb = new StringBuilder();
        
        sb.append("<h2>Sprig Boot Actuator</h2>");
        sb.append("<ul>");
        String url = "http://" + host + ":8090" + path + endPointPath;
        sb.append("<li><a href='" + url + "'>" + url + "</a></li>");
        sb.append("</ul>");
        return sb.toString();
    }
}

// 访问 http://localhost:8888/GetEndPoints
```

- **定义actuator/info特殊endpoint**

  ```
  actuator/info可以自定义一些信息
  
  书写如下代码即可访问
  
  package com.springlearn.learn.selfactuator;
  
  import java.util.HashMap;
  import java.util.Map;
  
  import org.springframework.boot.actuate.info.Info;
  import org.springframework.boot.actuate.info.InfoContributor;
  import org.springframework.stereotype.Component;
  
  @Component
  public class BuildInfoActuator implements InfoContributor{
  
      @Override
      public void contribute(Info.Builder builder) {
          Map<String,String> data= new HashMap<String,String>();
          data.put("build.version", "1.0.0");
          builder.withDetail("buildInfo", data);
      }
  }
  ```

  

- ## actuator/shutdown需要post请求才能访问

  ```
  可以用来关闭程序
  
  定义如下控制器即可
  
  package com.springlearn.learn.Controller;
  import org.springframework.http.HttpEntity;
  import org.springframework.http.HttpHeaders;
  import org.springframework.http.MediaType;
  import org.springframework.stereotype.Controller;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.ResponseBody;
  import org.springframework.web.client.RestTemplate;
  
  @Controller
  public class ShutDownController {
  
      @ResponseBody
      @RequestMapping(path = "/shutdown")
      public String actuatorShutdown(){
          String url = "http://localhost:8090/actuator/shutdown";
          HttpHeaders headers = new HttpHeaders();
          headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
          headers.setContentType(MediaType.APPLICATION_JSON);
  
          RestTemplate restTemplate = new RestTemplate();
          HttpEntity<String> requestBody = new HttpEntity<>("", headers);
          String e = restTemplate.postForObject(url, requestBody, String.class);
  
          return "Result: " + e;
      }
  }
  ```

  - 除了上面,也可以使用可视化界面springbootAdmin

    https://cloud.tencent.com/developer/article/1438675

## 13.niginx线程模型,如何实现高并发和高负载

https://blog.csdn.net/m0_38110132/article/details/75126316?utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-1.control&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-1.control

http://tengine.taobao.org/book/chapter_02.html

首先要明白，Nginx 采用的是多进程（单线程） & 多路IO复用模型。使用了 I/O 多路复用技术的 Nginx，就成了”并发事件驱动“的服务器。

![这里写图片描述](https://img-blog.csdn.net/20170303170640322?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvU1RGUEhQ/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

- **多进程的工作模式**

1、Nginx 在启动后，会有一个 master 进程和多个相互独立的 worker 进程。
2、接收来自外界的信号，向各worker进程发送信号，每个进程都有可能来处理这个连接。
3、 master 进程能监控 worker 进程的运行状态，当 worker 进程退出后(异常情况下)，会自动启动新的 worker 进程。

注意 worker 进程数，一般会设置成机器 cpu 核数。因为更多的worker 数，只会导致进程相互竞争 cpu，从而带来不必要的上下文切换。

使用多进程模式，不仅能提高并发率，而且进程之间相互独立，一个 worker 进程挂了不会影响到其他 worker 进程

- ## 惊群现象

  主进程（master 进程）首先通过 socket() 来创建一个 sock 文件描述符用来监听，然后fork生成子进程（workers 进程），子进程将继承父进程的 sockfd（socket 文件描述符），之后子进程 accept() 后将创建已连接描述符（connected descriptor）），然后通过已连接描述符来与客户端通信。

  那么，由于所有子进程都继承了父进程的 sockfd，那么当连接进来时，所有子进程都将收到通知并“争着”与它建立连接，这就叫“惊群现象”。大量的进程被激活又挂起，只有一个进程可以accept() 到这个连接，这当然会消耗系统资源

- **Nginx对惊群现象的处理**

  Nginx 提供了一个 accept_mutex 这个东西，这是一个加在accept上的一把共享锁。即每个 worker 进程在执行 accept 之前都需要先获取锁，获取不到就放弃执行 accept()。有了这把锁之后，同一时刻，就只会有一个进程去 accpet()，这样就不会有惊群问题了。accept_mutex 是一个可控选项，我们可以显示地关掉，默认是打开的

- Nginx的进程处理

- ## nignx负载均衡算法

  https://www.cnblogs.com/DarrenChan/p/8967412.html



## 14.分布式架构的强一致性算法

https://blog.csdn.net/weixin_41922289/article/details/107880919

## 15.SpringBoot自定义拦截器Inteceptor

https://www.cnblogs.com/javaxiaoxin/p/8275348.html

## 16.Spring中cglib动态代理导致注解失效

失效例子:

```
@Service
public class MarketMoneyChangeService{
    @DynamicReference
    private IMarketBuyerProvider marketBuyerProvider;
 
    @Transactional(rollbackFor = Exception.class)
    public Boolean doChangeMoney() {
 
        return true;
    }
 
}
```

注解声明如下:

```
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
 
/**
 * 服务动态注入注解
 */
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicReference {
 
}
```

代码中@DynamicReference为自定义注解, 作用是根据项目配置, 在运行时注入指定SpringBean或者Dubbo引用对象。正常情况下使用没问题, 但是当加上事务注解@Transactional时, MarketMoneyChangeService会变成cglib代理对象, 则使用Field无法获取到注解@DynamicReference

- 虽然使用了@Inherited注解,但是依然无法发射获取到注解,因为@Inherited只在类上使用被继承有效,关于@Inherited使用例子如下:

  - Inherited作用是，使用此注解声明出来的自定义注解，在使用此自定义注解时，如果注解在类上面时，子类会自动继承此注解，否则的话，子类不会继承此注解。这里一定要记住，使用Inherited声明出来的注解，只有在类上使用时才会有效，对方法，属性等其他无效。

    ```
    
    /**
     * 声明的此注解使用了Inherited元注解，表示此注解用在类上时，会被子类所继承
     * @author crazy
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public @interface InheritedTest {
     
    	String value();
    }
    ```

    ```
    /**
     * 声明的此注解没有使用Inherited元注解，表示此注解用在类上时，不会被子类所继承
     * @author crazy
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface InheritedTest2 {
     
    	String value();
    }
    ```

    ```
    /**
     * 父类
     * @author crazy
     */
    @InheritedTest("使用Inherited的注解 class")
    @InheritedTest2("未使用Inherited的注解 class")
    public class Parent {
     
    	@InheritedTest("使用Inherited的注解 method")
    	@InheritedTest2("未使用Inherited的注解 method")
    	public void method(){
    		
    	}
    	@InheritedTest("使用Inherited的注解 method2")
    	@InheritedTest2("未使用Inherited的注解 method2")
    	public void method2(){
    		
    	}
    	
    	@InheritedTest("使用Inherited的注解 field")
    	@InheritedTest2("未使用Inherited的注解 field")
    	public String a;
    }
    ```

    ```
    /**
     * 子类  只继承了一个method方法
     * @author crazy
     */
    public class Child extends Parent {
     
    	@Override
    	public void method() {
    	}
    }
    ```

    ```
    /**
     * 通过反射进行测试
     * @author crazy
     */
    public class test {
     
    	public static void main(String[] args) throws NoSuchMethodException, SecurityException, NoSuchFieldException {
    		Class<Child> clazz = Child.class;
    		//对类进行测试 
    		System.out.println("对类进行测试");
    		if(clazz.isAnnotationPresent(InheritedTest.class)){
    			System.out.println(clazz.getAnnotation(InheritedTest.class).value());
    		}
    		if(clazz.isAnnotationPresent(InheritedTest2.class)){
    			System.out.println(clazz.getAnnotation(InheritedTest2.class).value());
    		}
    		System.out.println();
    		//对方法 进行测试
    		System.out.println("对方法进行测试");
    		Method method = clazz.getMethod("method", null);
    		if(method.isAnnotationPresent(InheritedTest.class)){
    			System.out.println(method.getAnnotation(InheritedTest.class).value());
    		}
    		if(method.isAnnotationPresent(InheritedTest2.class)){
    			System.out.println(method.getAnnotation(InheritedTest2.class).value());
    		}
    		System.out.println();
    		//对方法2 进行测试
    		System.out.println("对方法2进行测试");
    		Method method2 = clazz.getMethod("method2", null);
    		if(method2.isAnnotationPresent(InheritedTest.class)){
    			System.out.println(method2.getAnnotation(InheritedTest.class).value());
    		}
    		if(method2.isAnnotationPresent(InheritedTest2.class)){
    			System.out.println(method2.getAnnotation(InheritedTest2.class).value());
    		}
    		System.out.println();
    		//对属性测试
    		System.out.println("对属性进行测试");
    		Field field = clazz.getField("a");
    		if(field.isAnnotationPresent(InheritedTest.class)){
    			System.out.println(field.getAnnotation(InheritedTest.class).value());
    		}
    		if(field.isAnnotationPresent(InheritedTest2.class)){
    			System.out.println(field.getAnnotation(InheritedTest2.class).value());
    		}
    	}
    }
    ```

    输出:

    ```
    对类进行测试
    使用Inherited的注解 class
    对方法进行测试
    对方法2进行测试
    使用Inherited的注解 method2
    未使用Inherited的注解 method2
    对属性进行测试
    使用Inherited的注解 field
    未使用Inherited的注解 field
    ```

    由上可以看出，通过Inherited元注解声明的自定义注解，在类上使用时，可以被子类继承，对第一个方法进行测试时，由于子类继承了父类方法，且两个都没有输出，证明Inherited对方法无效，由方法2可以看出，因为子类没有重写父类方法，所以是直接使用的父类方法，所以两个都会输出，同理属性也是，都会输出

## 17.获取Bean中所有的bean名称

- 方法一：

  实现ApplicationContextAware

  ```
  package com.example.demo.pojo;
  
  import org.springframework.beans.BeansException;
  import org.springframework.context.ApplicationContext;
  import org.springframework.context.ApplicationContextAware;
  import org.springframework.stereotype.Component;
  
  import java.util.HashSet;
  import java.util.Set;
  
  @Component
  public class TestEntity implements ApplicationContextAware {
      private Set<String> beans = new HashSet<>();
      @Override
      public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
          for (String s : applicationContext.getBeanDefinitionNames()
               ) {
              beans.add(s);
          }
      }
  }
  
  ```

  

- 方法二

  直接用@Autowired标记ApplicationContext,调用getBeanDefinitionNames

  ```
  package com.example.demo.pojo;
  
  import org.springframework.beans.BeansException;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.context.ApplicationContext;
  import org.springframework.stereotype.Component;
  
  
  @Component
  public class TestEntity {
      @Autowired
      private ApplicationContext applicationContext;
  }
  ```

## 18.长连接和短连接区别

https://www.liangzl.com/get-article-detail-199617.html

# 六.Mysql知识

- Sql语句复习

https://www.cnblogs.com/qluzzh/p/10782993.html

https://blog.csdn.net/u014398524/article/details/106505549/

## 1.mysql底层为什么用b+树而不用哈希表

```
和业务场景有关,如果只选一个数据,确实是hash更快,但是数据库中经常会选择多条，由于b+树的数据都存放在叶子节点并且有序,而且各个节点有链表相连,此时查询的效率比hash高.并且mysql索引一般存储在磁盘上，B+树可以允许数据分批加载,同时树的高度较低,查找效率高
```

## 2.mysql-日志类型

https://www.cnblogs.com/zhang-xiao-shuang/articles/12441308.html

MYSQL日志主要包括错误日志,查询日志,慢查询日志,事务日志,二进制日志

- 错误日志

```
错误日志信息可以自己进行配置的，错误日志所记录的信息是可以通过log-error和log-warnings来定义的，其中log-err是定义是否启用错误日志的功能和错误日志的存储位置，log-warnings是定义是否将警告信息也定义至错误日志中。默认情况下错误日志大概记录以下几个方面的信息：服务器启动和关闭过程中的信息（未必是错误信息，如mysql如何启动InnoDB的表空间文件的、如何初始化自己的存储引擎的等等）、服务器运行过程中的错误信息、事件调度器运行一个事件时产生的信息、在从服务器上启动服务器进程时产生的信息
```

```
错误日志可以使用log_err在主配置文件中手动设置
 
 
在my.cnf中
 
 
[mysqld_safe]
 
 
log_error=/usr/local/mysql/xxx.err（名字可以自己指定否则默认为主机名）修改完需要重启mysql生效。
 
 
mysql> show global variables like '%log%';
```

![img](https://img2020.cnblogs.com/i-beta/1956121/202003/1956121-20200308103255160-891189443.png)

## 3.聚集索引和非聚集索引区别

根本区别是表记录的排列顺序和索引的是否一致

- 聚集索引

  ```
  放在主键上
  聚集索引表的排列顺序和索引一致,查询效率快,只要找到第一个索引值,其余连续性的记录在物理内存也一样连续存放,聚集索引对应的缺点是修改慢,为了保证表中记录的物理和索引顺序一样，记录插入需要重新排序
  ```

- 非聚集索引

  ```
  放在非主键上,数据和索引排列顺序不一致,通过查询结果拿到主键值再去获取数据
  ```


## 4.Myisam和innodb的区别

- Myisam是5.1版本之前的默认引擎,但不支持事务和行级锁,所以一般有大量查询少量插入场景使用,myisam不支持外检,索引和数据分开存储

  myisam的表数据和结构在磁盘上分三个文件

  （1）frm文件：表的结构文件

  （2）MYD文件：存放表具体记录的数据

  （3）MYI文件：存储索引的文件

  ![img](https://img2020.cnblogs.com/blog/1552936/202008/1552936-20200808171322785-807215139.png)

  

  frm和MYI可以存放在不同的目录下。MYI文件用来存储索引，但仅保存记录所在页的指针，索引的结构是B+树结构。下面这张图就是MYI文件保存的机制

  ![img](https://img2020.cnblogs.com/blog/1552936/202008/1552936-20200808171231366-209640312.png)

- innodb基于聚簇索引建立,支持事务,外键,通过mvcc来支持高并发,索引和数据存储在一起

  他的表结构方法frm文件,数据和索引放一块在IBD文件

  ![img](https://img2020.cnblogs.com/blog/1552936/202008/1552936-20200808171435627-1680163116.png)

  ![img](https://img2020.cnblogs.com/blog/1552936/202008/1552936-20200808171526639-1493308398.png)

## 5.回表查询和索引覆盖

- 回表查询

  先定位到聚集索引值,再通过聚集索引值定位行记录,就叫回表查询

- 索引覆盖

  一次查询中,一个索引包含所有需要查询的字段的值,就是覆盖索引,不需要回表查询,通过explain + sql语句看extra结果是否是"Using index"即可

## 6.mysql锁的类型有哪些

mysql锁分为共享锁和排它锁,也叫读锁和写锁

- 读锁是共享的,通过lock in share mode，此时只能读不能写,也分行锁和表锁
- 写锁是排他的,会阻塞其他写锁和读锁.从颗粒度区分,分为表锁和行锁
  - 表锁锁定整张表，阻塞其他用户对该表的所有读写操作
  - 行锁分悲观锁和乐观锁,悲观锁通过for update实现,乐观锁通过版本号实现

## 7.事务的基本特性和隔离级别

https://www.cnblogs.com/jian-gao/p/10795407.html

- 事务基本特性

  - 原子性

    一个事务要么全部成功,要么全部失败

  - 一致性

    指数据库从一个状态转换到另一个状态

  - 隔离性

    一个事务修改提交前,对其他事务不可见

  - 持久性

    事务一旦提交,永久保存在数据库

- 隔离级别

  - 读未提交

    可以读到其他事务未提交的数据，这种也称为脏读

  - 读提交

    一个事务只能读到其他已提交的事务的数据,无法支持不可重复读,即其他势力处理期间读两次的结果不一致(前一次事务未提交,后面提交)

  - 可重复读

    Mysql的默认隔离级别,确保同一事物的多个实例并发读取数据能看到同样数据行,但会导致幻读,幻读就是用户读取某一行范围数据行,另一个事务又在该范围插入新行,用户再次读取有新的一行

  - 串行化

    强制事务的执行循序,解决脏读,不可重复读,幻读

## 8.mysql的mvcc的理解

https://blog.csdn.net/SnailMann/article/details/94724197

Mvcc即多版本并发控制技术,它使得大部分支持行锁的事务引擎，不再单纯的使用行锁来进行数据库的并发控制，取而代之的是把数据库的行锁与行的多个版本结合起来，只需要很小的开销,就可以实现非锁定读，从而大大提高数据库系统的并发性能

innodb的mvcc主要是为可重复读的隔离级别做的

## 9.Mysql的sql优化

https://blog.csdn.net/weixin_42047611/article/details/81772149

## 10.mysql删除一条数据是否是实时的

被删除的记录行,只是被标记删除,是可以复用的,下次有符合条件的记录可以直接插入到这个被标记的位置

比如我们在 id 为 300-600 之间的记录中删除一条 id=500 的记录，这条记录就会被标记为删除，等下一次如果有一条 id=400 的记录要插入进来，那么就可以复用 id=500 被标记删除的位置，这种情况叫**行记录复用。**

还有一种是数据的**页复用**,整个数据页都被标记删除,整个数据页都可以被复用,和行记录复用不同的是，数据页复用对要插入的数据几乎没有条件限制

还以上面那个插入为例，假如要插入的记录是 id=1000，那么就不能复用 id=500 这个位置了，但如果有一整个数据页可复用的话，那么无论 id 值为多少都可以被复用在这个页上

这些被标记删除的记录，其实就是一个空洞，有种占着茅坑不拉屎的感觉，浪费空间不说，还会影响查询效率。

因为你要知道，mysql 在底层是以数据页为单位来存储和读取数据的，每次向磁盘读一次数据就是读一个数据页，然而每访问一个数据页就对应一次磁盘 IO 操作，磁盘 IO 相对内存访问速度是相当慢的。

所以你想想，如果一个表上存在大量的数据空洞，原本只需一个数据页就保存的数据，由于被很多空洞占用了空间，不得不需要增加其他的数据页来保存数据，相应的，mysql 在查询相同数据的时候，就不得不增加磁盘 IO 操作，从而影响查询速度。

因此，一个数据表在经过大量频繁的增删改之后，难免会产生数据空洞，浪费空间并影响查询效率，通常在生产环境中会直接表现为原本很快的查询会变得越来越慢。

对于这种情况，我们通常可以使用下面这个命令就能解决数据空洞问题。

```
optimize table t
```


这个命令的原理就是重建表，就是建立一个临时表 B，然后把表 A（存在数据空洞的表） 中的所有数据查询出来，接着把数据全部重新插入到临时表 B 中，最后再用临时表 B 替换表 A 即可，这就是重建表的过程。

## 11.sql语句练习

https://blog.csdn.net/paul0127/article/details/82529216

# 七.数据结构知识

## 1.红黑树

```
红黑是相对平衡的二叉排序树
特性:
(1)每个节点是黑色,或者红色
(2)根节点是黑色
(3)如果父节点是红色，那么子节点必须是红色
(4)每个叶子节点都是黑色
(5)从一个节点到该节点的子孙节点的所有路径包含相同数目的黑节点

特性5确保没有一条路径会比其他路径长两倍
```

![img](https://images0.cnblogs.com/i/497634/201403/251730074203156.jpg)

https://www.cnblogs.com/skywang12345/p/3624343.html

# 八.JavaIO和NIO,日志记录

## 1.@Slf4j使用

```
         <dependency>
             <groupId>org.projectlombok</groupId>
             <artifactId>lombok</artifactId>
             <version>1.18.12</version>
             <scope>test</scope>
         </dependency>
```

![image-20210406203415251](C:\Users\hasee\AppData\Roaming\Typora\typora-user-images\image-20210406203415251.png)

```java
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class test {

    @Test
    public void test() throws InterruptedException {
        log.info("hello world");
    }

}

```

使用logback来集成@Slf4j,引入如下三个依赖

```xml
         <dependency>
             <groupId>ch.qos.logback</groupId>
             <artifactId>logback-core</artifactId>
             <version>1.3.0-alpha5</version>
         </dependency>
         <dependency>
             <groupId>ch.qos.logback</groupId>
             <artifactId>logback-classic</artifactId>
             <version>1.3.0-alpha5</version>
         </dependency>
         <dependency>
             <groupId>ch.qos.logback</groupId>
             <artifactId>logback-access</artifactId>
             <version>1.3.0-alpha5</version>
         </dependency>
```

**一、logback的介绍**
　　Logback是由log4j创始人设计的另一个开源日志组件,官方网站： http://logback.qos.ch。它当前分为下面下个模块：
　　logback-core：其它两个模块的基础模块
　　logback-classic：它是log4j的一个改良版本，同时它完整实现了slf4j API使你可以很方便地更换成其它日志系统如log4j或JDK14 Logging
　　logback-access：访问模块与Servlet容器集成提供通过Http来访问日志的功能

**二、logback取代log4j的理由：**
　　1、更快的实现：Logback的内核重写了，在一些关键执行路径上性能提升10倍以上。而且logback不仅性能提升了，初始化内存加载也更小了。
　　2、非常充分的测试：Logback经过了几年，数不清小时的测试。Logback的测试完全不同级别的。
　　3、Logback-classic非常自然实现了SLF4j：Logback-classic实现了SLF4j。在使用SLF4j中，你都感觉不到logback-classic。而且因为logback-classic非常自然地实现了slf4j ， 所 以切换到log4j或者其他，非常容易，只需要提供成另一个jar包就OK，根本不需要去动那些通过SLF4JAPI实现的代码。
　　4、非常充分的文档 官方网站有两百多页的文档。
　　5、自动重新加载配置文件，当配置文件修改了，Logback-classic能自动重新加载配置文件。扫描过程快且安全，它并不需要另外创建一个扫描线程。这个技术充分保证了应用程序能跑得很欢在JEE环境里面。
　　6、Lilith是log事件的观察者，和log4j的chainsaw类似。而lilith还能处理大数量的log数据 。
　　7、谨慎的模式和非常友好的恢复，在谨慎模式下，多个FileAppender实例跑在多个JVM下，能 够安全地写道同一个日志文件。RollingFileAppender会有些限制。Logback的FileAppender和它的子类包括 RollingFileAppender能够非常友好地从I/O异常中恢复。
　　8、配置文件可以处理不同的情况，开发人员经常需要判断不同的Logback配置文件在不同的环境下（开发，测试，生产）。而这些配置文件仅仅只有一些很小的不同，可以通过,和来实现，这样一个配置文件就可以适应多个环境。
　　9、Filters（过滤器）有些时候，需要诊断一个问题，需要打出日志。在log4j，只有降低日志级别，不过这样会打出大量的日志，会影响应用性能。在Logback，你可以继续 保持那个日志级别而除掉某种特殊情况，如alice这个用户登录，她的日志将打在DEBUG级别而其他用户可以继续打在WARN级别。要实现这个功能只需加4行XML配置。可以参考MDCFIlter 。
　　10、SiftingAppender（一个非常多功能的Appender）：它可以用来分割日志文件根据任何一个给定的运行参数。如，SiftingAppender能够区别日志事件跟进用户的Session，然后每个用户会有一个日志文件。
　　11、自动压缩已经打出来的log：RollingFileAppender在产生新文件的时候，会自动压缩已经打出来的日志文件。压缩是个异步过程，所以甚至对于大的日志文件，在压缩过程中应用不会受任何影响。
　　12、堆栈树带有包版本：Logback在打出堆栈树日志时，会带上包的数据。
　　13、自动去除旧的日志文件：通过设置TimeBasedRollingPolicy或者SizeAndTimeBasedFNATP的maxHistory属性，你可以控制已经产生日志文件的最大数量。如果设置maxHistory 12，那那些log文件超过12个月的都会被自动移除。

**三、logback的配置介绍**
　　1、Logger、appender及layout
Logger作为日志的记录器，把它关联到应用的对应的context上后，主要用于存放日志对象，也可以定义日志类型、级别。
Appender主要用于指定日志输出的目的地，目的地可以是控制台、文件、远程套接字服务器、 MySQL、PostreSQL、 Oracle和其他数据库、 JMS和远程UNIX Syslog守护进程等。 
Layout 负责把事件转换成字符串，格式化的日志信息的输出。
　　2、logger context
各个logger 都被关联到一个 LoggerContext，LoggerContext负责制造logger，也负责以树结构排列各logger。其他所有logger也通过org.slf4j.LoggerFactory 类的静态方法getLogger取得。 getLogger方法以 logger名称为参数。用同一名字调用LoggerFactory.getLogger 方法所得到的永远都是同一个logger对象的引用。
　　3、有效级别及级别的继承
Logger 可以被分配级别。级别包括：TRACE、DEBUG、INFO、WARN 和 ERROR，定义于ch.qos.logback.classic.Level类。如果 logger没有被分配级别，那么它将从有被分配级别的最近的祖先那里继承级别。root logger 默认级别是 DEBUG。
　　4、打印方法与基本的选择规则
打印方法决定记录请求的级别。例如，如果 L 是一个 logger 实例，那么，语句 L.info("..")是一条级别为 INFO的记录语句。记录请求的级别在高于或等于其 logger 的有效级别时被称为被启用，否则，称为被禁用。记录请求级别为 p，其 logger的有效级别为 q，只有则当 p>=q时，该请求才会被执行。
该规则是 logback 的核心。级别排序为： TRACE < DEBUG < INFO < WARN < ERROR

**四、logback的默认配置**

如果配置文件 logback-test.xml 和 logback.xml 都不存在，那么 logback 默认地会调用BasicConfigurator ，创建一个最小化配置。最小化配置由一个关联到根 logger 的ConsoleAppender 组成。输出用模式为%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n 的 PatternLayoutEncoder 进行格式化。root logger 默认级别是 DEBUG。
　　1、Logback的配置文件
Logback 配置文件的语法非常灵活。正因为灵活，所以无法用 DTD 或 XML schema 进行定义。尽管如此，可以这样描述配置文件的基本结构：以<configuration>开头，后面有零个或多个<appender>元素，有零个或多个<logger>元素，有最多一个<root>元素。
　　2、Logback默认配置的步骤
　　　　(1). 尝试在 classpath下查找文件logback-test.xml；
　　　　(2). 如果文件不存在，则查找文件logback.xml；
　　　　(3). 如果两个文件都不存在，logback用BasicConfigurator自动对自己进行配置，这会导致记录输出到控制台

**五、logback.xml常用配置详解**

![img](https://images2015.cnblogs.com/blog/786086/201607/786086-20160728133120325-2005893017.png)

```xml

　　1、根节点<configuration>，包含下面三个属性：
　　　　scan: 当此属性设置为true时，配置文件如果发生改变，将会被重新加载，默认值为true。
　　　　scanPeriod: 设置监测配置文件是否有修改的时间间隔，如果没有给出时间单位，默认单位是毫秒。当scan为true时，此属性生效。默认的时间间隔为1分钟。
　　　　debug: 当此属性设置为true时，将打印出logback内部日志信息，实时查看logback运行状态。默认值为false。
　　例如：
　　　　<configuration scan="true" scanPeriod="60 seconds" debug="false"> 
　　　　　　<!--其他配置省略--> 
　　　　</configuration>
　　2、子节点<contextName>：用来设置上下文名称，每个logger都关联到logger上下文，默认上下文名称为default。但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
　　例如：
　　　　<configuration scan="true" scanPeriod="60 seconds" debug="false"> 
　　　　　　<contextName>myAppName</contextName> 
　　　　　　<!--其他配置省略-->
　　　　</configuration>
　　3、子节点<property> ：用来定义变量值，它有两个属性name和value，通过<property>定义的值会被插入到logger上下文中，可以使“${}”来使用变量。
　　　　name: 变量的名称
　　　　value: 的值时变量定义的值
　　例如：
　　　　<configuration scan="true" scanPeriod="60 seconds" debug="false"> 
　　　　　　<property name="APP_Name" value="myAppName" /> 
　　　　　　<contextName>${APP_Name}</contextName> 
　　　　　　<!--其他配置省略--> 
　　　　</configuration>
　　4、子节点<timestamp>：获取时间戳字符串，他有两个属性key和datePattern
　　　　key: 标识此<timestamp> 的名字；
　　　　datePattern: 设置将当前时间（解析配置文件的时间）转换为字符串的模式，遵循java.txt.SimpleDateFormat的格式。
　　例如：
　　　　<configuration scan="true" scanPeriod="60 seconds" debug="false"> 
　　　　　　<timestamp key="bySecond" datePattern="yyyyMMdd'T'HHmmss"/> 
　　　　　　<contextName>${bySecond}</contextName> 
　　　　　　<!-- 其他配置省略--> 
　　　　</configuration>
　　5、子节点<appender>：负责写日志的组件，它有两个必要属性name和class。name指定appender名称，class指定appender的全限定名
　　　　5.1、ConsoleAppender 把日志输出到控制台，有以下子节点：
　　　　　　<encoder>：对日志进行格式化。（具体参数稍后讲解 ）
　　　　　　<target>：字符串System.out(默认)或者System.err（区别不多说了）
　　　　例如：
　　　　<configuration> 
　　　　　　<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender"> 
　　　　　　<encoder> 
　　　　　　　　<pattern>%-4relative [%thread] %-5level %logger{35} - %msg %n</pattern> 
　　　　　　</encoder> 
　　　　　　</appender> 

　　　　　　<root level="DEBUG"> 
　　　　　　　　<appender-ref ref="STDOUT" /> 
　　　　　　</root> 
　　　　</configuration>
　　　　上述配置表示把>=DEBUG级别的日志都输出到控制台

　　　　5.2、FileAppender：把日志添加到文件，有以下子节点：
　　　　　　<file>：被写入的文件名，可以是相对目录，也可以是绝对目录，如果上级目录不存在会自动创建，没有默认值。
　　　　　　<append>：如果是 true，日志被追加到文件结尾，如果是 false，清空现存文件，默认是true。
　　　　　　<encoder>：对记录事件进行格式化。（具体参数稍后讲解 ）
　　　　　　<prudent>：如果是 true，日志会被安全的写入文件，即使其他的FileAppender也在向此文件做写入操作，效率低，默认是 false。
　　　　例如：
　　　　<configuration> 
　　　　　　<appender name="FILE" class="ch.qos.logback.core.FileAppender"> 
　　　　　　　　<file>testFile.log</file> 
　　　　　　　　<append>true</append> 
　　　　　　　　<encoder> 
　　　　　　　　　　<pattern>%-4relative [%thread] %-5level %logger{35} - %msg%n</pattern> 
　　　　　　　　</encoder> 
　　　　　　</appender> 

　　　　　　<root level="DEBUG"> 
　　　　　　<appender-ref ref="FILE" /> 
　　　　　　</root> 
　　　　</configuration>
　　　　上述配置表示把>=DEBUG级别的日志都输出到testFile.log

　　　　5.3、RollingFileAppender：滚动记录文件，先将日志记录到指定文件，当符合某个条件时，将日志记录到其他文件。有以下子节点：
　　　　　　<file>：被写入的文件名，可以是相对目录，也可以是绝对目录，如果上级目录不存在会自动创建，没有默认值。
　　　　　　<append>：如果是 true，日志被追加到文件结尾，如果是 false，清空现存文件，默认是true。
　　　　　　<rollingPolicy>:当发生滚动时，决定RollingFileAppender的行为，涉及文件移动和重命名。属性class定义具体的滚动策略类
　　　　　　class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy"： 最常用的滚动策略，它根据时间来制定滚动策略，既负责滚动也负责出发滚动。有以下子节点：
　　　　　　　　<fileNamePattern>：必要节点，包含文件名及“%d”转换符，“%d”可以包含一个java.text.SimpleDateFormat指定的时间格式，如：%d{yyyy-MM}。
如果直接使用 %d，默认格式是 yyyy-MM-dd。RollingFileAppender的file字节点可有可无，通过设置file，可以为活动文件和归档文件指定不同位置，当前日志总是记录到file指定的文件（活动文件），活动文件的名字不会改变；
如果没设置file，活动文件的名字会根据fileNamePattern 的值，每隔一段时间改变一次。“/”或者“\”会被当做目录分隔符。
　　　　　　　　<maxHistory>:
可选节点，控制保留的归档文件的最大数量，超出数量就删除旧文件。假设设置每个月滚动，且<maxHistory>是6，则只保存最近6个月的文件，删除之前的旧文件。注意，删除旧文件是，那些为了归档而创建的目录也会被删除。

　　　　　　class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy"： 查看当前活动文件的大小，如果超过指定大小会告知RollingFileAppender 触发当前活动文件滚动。只有一个节点:
　　　　　　　　<maxFileSize>:这是活动文件的大小，默认值是10MB。
　　　　　　　　<prudent>：当为true时，不支持FixedWindowRollingPolicy。支持TimeBasedRollingPolicy，但是有两个限制，1不支持也不允许文件压缩，2不能设置file属性，必须留空。

　　　　　　<triggeringPolicy >: 告知 RollingFileAppender 合适激活滚动。
　　　　　　class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy" 根据固定窗口算法重命名文件的滚动策略。有以下子节点：
　　　　　　　　<minIndex>:窗口索引最小值
　　　　　　　　<maxIndex>:窗口索引最大值，当用户指定的窗口过大时，会自动将窗口设置为12。
　　　　　　　　<fileNamePattern>:必须包含“%i”例如，假设最小值和最大值分别为1和2，命名模式为 mylog%i.log,会产生归档文件mylog1.log和mylog2.log。还可以指定文件压缩选项，例如，mylog%i.log.gz 或者 没有log%i.log.zip
　　　　　　例如：
　　　　　　　　<configuration> 
　　　　　　　　　　<appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender"> 
　　　　　　　　　　　　<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy"> 
　　　　　　　　　　　　　　<fileNamePattern>logFile.%d{yyyy-MM-dd}.log</fileNamePattern> 
　　　　　　　　　　　　　　<maxHistory>30</maxHistory> 
　　　　　　　　　　　　</rollingPolicy> 
　　　　　　　　　　　　<encoder> 
　　　　　　　　　　　　　　<pattern>%-4relative [%thread] %-5level %logger{35} - %msg%n</pattern> 
　　　　　　　　　　　　</encoder> 
　　　　　　　　　　</appender> 

　　　　　　　　　　<root level="DEBUG"> 
　　　　　　　　　　　　<appender-ref ref="FILE" /> 
　　　　　　　　　　</root> 
　　　　　　　　</configuration>
　　　　　　　　上述配置表示每天生成一个日志文件，保存30天的日志文件。
　　　　　　　　<configuration> 
　　　　　　　　　　<appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender"> 
　　　　　　　　　　　　<file>test.log</file> 

　　　　　　　　　　　　<rollingPolicy class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy"> 
　　　　　　　　　　　　　　<fileNamePattern>tests.%i.log.zip</fileNamePattern> 
　　　　　　　　　　　　　　<minIndex>1</minIndex> 
　　　　　　　　　　　　　　<maxIndex>3</maxIndex> 
　　　　　　　　　　　　</rollingPolicy> 

　　　　　　　　　　　　<triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy"> 
　　　　　　　　　　　　　　<maxFileSize>5MB</maxFileSize> 
　　　　　　　　　　　　</triggeringPolicy> 
　　　　　　　　　　　　<encoder> 
　　　　　　　　　　　　　　<pattern>%-4relative [%thread] %-5level %logger{35} - %msg%n</pattern> 
　　　　　　　　　　　　</encoder> 
　　　　　　　　　　</appender> 

　　　　　　　　　　<root level="DEBUG"> 
　　　　　　　　　　　　<appender-ref ref="FILE" /> 
　　　　　　　　　　</root> 
　　　　　　　　</configuration>
　　　　　　　　上述配置表示按照固定窗口模式生成日志文件，当文件大于20MB时，生成新的日志文件。窗口大小是1到3，当保存了3个归档文件后，将覆盖最早的日志。
　　　　　　<encoder>：对记录事件进行格式化。负责两件事，一是把日志信息转换成字节数组，二是把字节数组写入到输出流。
PatternLayoutEncoder 是唯一有用的且默认的encoder ，有一个<pattern>节点，用来设置日志的输入格式。使用“%”加“转换符”方式，如果要输出“%”，则必须用“\”对“\%”进行转义。
　　　　5.4、还有SocketAppender、SMTPAppender、DBAppender、SyslogAppender、SiftingAppender，并不常用，这里就不详解了。
大家可以参考官方文档（http://logback.qos.ch/documentation.html），还可以编写自己的Appender。
　　6、子节点<loger>：用来设置某一个包或具体的某一个类的日志打印级别、以及指定<appender>。<loger>仅有一个name属性，一个可选的level和一个可选的addtivity属性。
可以包含零个或多个<appender-ref>元素，标识这个appender将会添加到这个loger
　　　　name: 用来指定受此loger约束的某一个包或者具体的某一个类。
　　　　level: 用来设置打印级别，大小写无关：TRACE, DEBUG, INFO, WARN, ERROR, ALL和OFF，还有一个特俗值INHERITED或者同义词NULL，代表强制执行上级的级别。 如果未设置此属性，那么当前loger将会继承上级的级别。
addtivity: 是否向上级loger传递打印信息。默认是true。同<loger>一样，可以包含零个或多个<appender-ref>元素，标识这个appender将会添加到这个loger。
　　7、子节点<root>:它也是<loger>元素，但是它是根loger,是所有<loger>的上级。只有一个level属性，因为name已经被命名为"root",且已经是最上级了。
　　　　level: 用来设置打印级别，大小写无关：TRACE, DEBUG, INFO, WARN, ERROR, ALL和OFF，不能设置为INHERITED或者同义词NULL。 默认是DEBUG。
```

**六、常用loger配置**

```
<!-- show parameters for hibernate sql 专为 Hibernate 定制 -->
<logger name="org.hibernate.type.descriptor.sql.BasicBinder" level="TRACE" />
<logger name="org.hibernate.type.descriptor.sql.BasicExtractor" level="DEBUG" />
<logger name="org.hibernate.SQL" level="DEBUG" />
<logger name="org.hibernate.engine.QueryParameters" level="DEBUG" />
<logger name="org.hibernate.engine.query.HQLQueryPlan" level="DEBUG" />

<!--myibatis log configure-->
<logger name="com.apache.ibatis" level="TRACE"/>
<logger name="java.sql.Connection" level="DEBUG"/>
<logger name="java.sql.Statement" level="DEBUG"/>
<logger name="java.sql.PreparedStatement" level="DEBUG"/>
```

**七、Demo**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration scan="false" scanPeriod="60000" debug="false">
    <!--定义日志文件的存储地址 勿在 LogBack 的配置中使用相对路径-->
    <property name="LOG_HOME" value="D:/log" />
    <!-- 控制台输出 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
    </appender>
    <!-- 按照每天生成日志文件 -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <FileNamePattern>${LOG_HOME}/TestWeb.log.%d{yyyy-MM-dd}.log</FileNamePattern>
            <!--日志文件保留天数-->
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
        <!--日志文件最大的大小-->
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <MaxFileSize>10MB</MaxFileSize>
        </triggeringPolicy>
    </appender>
    <!-- 日志输出级别 -->
    <root level="INFO">
        <appender-ref ref="FILE" />
    </root>
</configuration>
```

# 九.Redis技术

https://blog.csdn.net/bird73/article/details/79792548

## 1.String类型的底层实现:

```
简单动态字符串SDS,优点:
1.更高效地执行长度计算
2.高效执行追加操作
3.二进制安全
```

## 2.Redis的数据结构类型和底层

https://www.cnblogs.com/haoprogrammer/p/11065461.html

https://www.cnblogs.com/ysocean/p/9080942.html

```
1.String 字符串
2.Lists 列表
3.Set 集合
4.sorted Set 有序集合
5.Hash 哈希表
```

底层数据结构一共有6种,分别是简单动态字符串,双向链表,压缩列表,哈希表,跳表,整数数组

对应关系:

```
String:简单动态字符串
list:双向链表，压缩列表
hash:压缩列表,哈希表
sorted set:压缩列表,跳表
set:哈希表,整数数组
```

**键和值用什么结构组织？**

Redis用一个哈希表保存所有键值对

一个哈希表就是一个数组,数组每个元素是个哈希桶,每个哈希桶保存了键值对数据

哈希桶中元素保存的不是值本身,是指向具体值的指针,不管值是String，还是集合类型,哈希桶中元素都是指向它们的指针

![img](https://img-blog.csdnimg.cn/img_convert/a8922fe7f12d9f2f520733248d926f7e.png)

因为这个哈希表保存了所有的键值对，所以，我也把它称为全局哈希表。哈希表的最大好处很明显，就是让我们可以用 O(1) 的时间复杂度来快速查找到键值对——我们只需要计算键的哈希值，就可以知道它所对应的哈希桶位置，然后就可以访问相应的 entry 元素。

- Redis解决哈希冲突用的链表哈希,但是发生哈希冲突越多,查询效率越慢,桶是基于链表的，所以又做了**rehash**再哈希的操作,rehash就是增加桶的数量,延长哈希表的数组长度,重新计算哈希值,为了使rehash更高效,redis使用两个全局哈希表,插入数据默认使用哈希表1,哈希表2在数据太多的时候会从哈希表1拷贝数据过去,这一个过程会导致redis线程阻塞,所以又使用渐进式rehash,就是在拷贝数据时,redis扔正常处理客户端请求,没处理一个请求,从哈希表1中第一个索引开始,从这个索引上把所有entries拷贝到哈希表2,等下个请求再到下个索引拷贝,如图
- ![img](https://img-blog.csdnimg.cn/img_convert/a21d9c5f61559c821e46dc31193c4b93.png)

巧妙地把一次性大量拷贝的开销，分摊到了多次处理请求的过程中，避免了耗时操作，保证了数据的快速访问



## 3.redis的几种模式

https://www.cnblogs.com/zhonglongbo/p/13128955.html

![img](https://img-blog.csdnimg.cn/20190114114112677.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTIwNDUwNDU=,size_16,color_FFFFFF,t_70)

### 1.主从模式:

```
一个Redis实例作为主机,其余作为备机,主节点可以读和写,从节点负责同步和读。客户端将数据写入主机,主机自动将数据同步到从机。主从模式可以达到读写分离的目的。
```

缺点:

- 一旦主节点宕机,从节点升为主节点,要人工修改应用方的地址,还需要命令所有从节点去复制新主节点
- 主节点写能力受单机限制
- 主节点存储能力受单机限制

### 2.单机模式

缺点:

```
局限于cpu的处理能力和内存
```

### 3.哨兵模式

![img](https://tva1.sinaimg.cn/large/007S8ZIlgy1gfitueyqq3j30ys0n476e.jpg)

如图,哨兵模式有两部分,哨兵节点和数据节点

- 哨兵节点:哨兵系统由一个或多个哨兵节点组成,不存储数据
- 数据节点:主节点和从节点都是数据节点

访问redis集群数据都是要通过哨兵节点,哨兵节点监控整个redis集群,一旦主节点挂了,从节点会顶上来,应用程序不需再修改ip，因为是通过哨兵节点访问

哨兵节点功能:

- 主节点存活检测
- 主从运行情况检测
- 主从切换

**哨兵模式的监控原理:**

https://www.cnblogs.com/zhonglongbo/p/13128955.html

![img](https://tva1.sinaimg.cn/large/007S8ZIlgy1gfixu17nfyj30vg0ng41n.jpg)

```
1.每个哨兵每秒钟一次向所有主服务器和从服务器,以及其他哨兵实例发送一个ping命令

2.如果一个实例距离最后一次有效回复ping的时间超过down-after-milliseconds所指定的值,那么这个实例会被标记为主观下线

3.如果一个主服务器被标记主观下线,那正在见识这个主服务器的所有哨兵节点要以每秒一次的频率确认该主服务器是否进入主观下线的状态

4.如果一个主服务器被标记主观下线,并且有足够数量的哨兵(至少达到配置文件指定数量)在指定时间范围内同意这一判断,那这主服务器被定为客观下线

5.在一般情况,每隔哨兵节点都会以每10秒一次的频率,向她已知所有主服务器和从服务器发送info命令
6.当一个主服务器被哨兵标记客观下线,哨兵节点向下线主服务器的所有从服务器发送的info命令频率改为1秒1次
7.哨兵和其他哨兵协商主节点的状态,如果主节点处理客观下线,投票自动选出新主节点,并将剩余从节点指向新的主节点进行数据复制
8.当没有足够数量哨兵统一主服务器下线,主服务器客观下线状态被移除,当主服务器向哨兵的ping命令返回有效时间时,主观下线状态也会被移除
```

哨兵模式的优缺点:

优点:

- 哨兵模式基于主从模式,继承主从优点
- 主从可以自动切换,不需要人工
- 哨兵会不断检查主服务器和从服务器是否正常运行,当被监控的某个redis服务器出现问题,哨兵通过Api脚本向管理员或其他应用程序发送通知

缺点:

- Redis较难支持在线扩容,对于集群,容量到达上限扩容比较困难

### 4.集群模式

集群模式主要解决哨兵和主从单个节点存储能力和访问能力上限的问题以及哨兵模式扩容困难

**基本原理:**

```
通过数据分片的方式来进行数据共享问题,同时提供数据复制和故障转移功能,之前两种模式数据都是在一个节点上,单个节点存储存在上限,集群模式把数据分片,当一个分片数据达到上限,就分成多个分片
```

数据分片怎么分

集群的键空间分割为16384个hash槽,通过hash方式将数据分到不同分片

```
Hash_SLOT = CRC16(key) & 16384
```

- 为何选择16384?

  ```
  1.集群节点数基本不会超1000,超过1000会导致网络拥堵,16384个hash槽基本足够,
  2.槽位太大,发送心跳信息头太大,浪费带宽,redis节点需要发送一定数量ping消息作为心跳包
  3.槽位越小,节点少的情况,压缩比高,redis主节点配置信息中哈希槽是通过一张bitmap形式来保存的,在传输过程中,会对bitmap进行压缩,如果bitmap填充率hash槽/N很高(N表示节点数),bitmap压缩率就很低（压缩率指文件压缩前后的大小比）
  ```

  使用位运算取得取模结果,位运算速度快

![img](https://tva1.sinaimg.cn/large/007S8ZIlgy1gfjigqb0omj315w0pc776.jpg)

**数据分片后怎么查,怎么写**

![img](https://tva1.sinaimg.cn/large/007S8ZIlgy1gfjiydi719j31980pk426.jpg)

读请求分配给slave节点,写请求分配给master,数据同步从master到slave节点

**如何做到水平扩展**

![img](https://tva1.sinaimg.cn/large/007S8ZIlgy1gfjj6f6zcqj318h0u043d.jpg)

master节点可以作扩充,数据迁移redis内部自动完成

```
当新增一个master节点,做数据迁移,redis服务不需要下线

举个栗子：上面有是哪个master，以为redis槽被分为3个段，假设三段分别为0-7000,7001-12000,12000-16383

现在因为业务需要新增了一个master节点,四个节点共同占有16384个槽，进行了重新分配

redis集群的重新分片由redis内部的管理软件redis-trib负责执行.redis提供了进行重新分片的所有命令,redis-trib通过向节点发送命令来进行重新分片
```

**如何做故障转移:**

![img](https://tva1.sinaimg.cn/large/007S8ZIlgy1gfjjuellcgj316q0u0gqk.jpg)

加入途中红色的节点故障了,此时master3下面的从节点会通过选举产生一个主节点.替换原来的故障节点,此过程跟哨兵模式故障转移一致

## 4.Redis的过期策略类型

1.定时删除

- 含义：在设置key的过期时间的同时，为该key创建一个定时器，让定时器在key的过期时间来临时，对key进行删除

- 优点：保证内存被尽快释放

- 缺点

  1.若过期key很多，删除这些key会占用很多的CPU时间

  2.定时器的创建耗时,每一个key设置过期时间都会创建一个定时器

2.惰性删除

- 含义：key过期的时候不删除，每次从数据库获取key的时候去检查是否过期，若过期，则删除，返回null。
- 优点：删除操作只发生在从数据库取出key的时候发生，而且只删除当前key，所以对CPU时间的占用是比较少的
- 缺点：若大量的key在超出超时时间后，很久一段时间内，都没有被获取过，那么可能发生内存泄露（无用的垃圾占用了大量的内存

3.定期删除

- 含义：每隔一段时间执行一次删除(在redis.conf配置文件设置hz，1s刷新的频率)过期key操作
- 优点:
  - 通过限制删除操作的时长和频率,减少删除操作对cpu时间的占用
  - 可以防止内存泄漏

- 总结：

  1.定时删除和定期删除为主动删除,Redis会定期淘汰一批过去的key

  2.惰性删除为被动删除,用到的时候才回去检查key是不是已过期,过期则删除

  3.惰性删除为redis服务器内置策略

  4.定期删除可以通过:

  -   配置redis.conf的hz选项,默认为10(即1秒执行10次),值越大刷新频率越快,redis性能损耗越大
  - 配置redis.conf的maxmemory最大值,当已用内存超过maxmemory限定,就会触发主动清理策略

## 5.Redis采用的过期策略

惰性删除+定期删除

- 惰性删除流程
  - 进行get或setnx操作，先检查key是否过期
  - 若过期,删除key,然后执行相应操作
  - 若没过期,直接执行相应操作
- 定期删除流程(对指定个数每一个库随机删除小于等于指定个数过期key)
  - 检查当前库中指定个数key(默认是每个库检查20个key.注意相当于如下循环执行20次)
    - 如果当前库中没有一个key设置过期时间,直接执行下一个库遍历
    - 随机获取一个设置了过期时间的key,检查该key是否过期,过期,则删除key
    - 判断定期删除操作是否达到指定时长,若达到,直接退出定期删除

## 6.RDB对过期key的处理

过期key对RDB没有任何影响

- 从内存数据库持久化数据到RDB文件前,会检查key是否过期,过期key不进入RDB文件
- 从RDB文件恢复数据到内存数据库前,会对key先进行过期检查,如果过期,不导入数据库

## 7.AOF对过期key的处理

过期key对AOF没有任何影响:

- 从内存数据库持久化数据AOF文件:
  - 当key过期后,还没有被删除,此时进行执行持久化操作(该key不会进入aof文件，没有发生修改命令)
  - 当key过期后,在发生删除操作时,程序向aof文件追加一条del命令(将来aof文件恢复数据时,过期的key会被删除)

## 8.Redis的缓存击穿,缓存穿透,缓存雪崩现象和解决方案

- 缓存穿透：key对应的数据在数据源并不存在，每次针对此key的请求从缓存获取不到，请求都会到数据源，从而可能压垮数据源。比如用一个不存在的用户id获取用户信息，不论缓存还是数据库都没有，若黑客利用此漏洞进行攻击可能压垮数据库。
- 缓存击穿：key对应的数据存在，但在redis中过期，此时若有大量并发请求过来，这些请求发现缓存过期一般都会从后端DB加载数据并回设到缓存，这个时候大并发的请求可能会瞬间把后端DB压垮。
- 缓存雪崩：当缓存服务器重启或者大量缓存集中在某一个时间段失效，这样在失效的时候，也会给后端系统(比如DB)带来很大压力。

**缓存穿透解决方案:**

```
最常见的是采用布隆过滤器，将所有可能存在的数据哈希到一个足够大的bitmap中，一个一定不存在的数据会被 这个bitmap拦截掉，从而避免了对底层存储系统的查询压力。另外也有一个更为简单粗暴的方法（我们采用的就是这种），如果一个查询返回的数据为空（不管是数据不存在，还是系统故障），我们仍然把这个空结果进行缓存，但它的过期时间会很短
```

**缓存击穿解决方案:**

```
业界比较常用的做法，是使用mutex。简单地来说，就是在缓存失效的时候（判断拿出来的值为空），不是立即去load db，而是先使用缓存工具的某些带成功操作返回值的操作（比如Redis的SETNX或者Memcache的ADD）去set一个mutex key，当操作返回成功时，再进行load db的操作并回设缓存；否则，就重试整个get缓存的方法。
```

redis伪代码:

```
public String get(key) {
      String value = redis.get(key);
      if (value == null) { //代表缓存值过期
          //设置3min的超时，防止del操作失败的时候，下次缓存过期一直不能load db
      if (redis.setnx(key_mutex, 1, 3 * 60) == 1) {  //代表设置成功
               value = db.get(key);
                      redis.set(key, value, expire_secs);
                      redis.del(key_mutex);
              } else {  //这个时候代表同时候的其他线程已经load db并回设到缓存了，这时候重试获取缓存值即可
                      sleep(50);
                      get(key);  //重试
              }
          } else {
              return value;      
          }
 }
```

memcache伪代码:

```
if (memcache.get(key) == null) {  
    // 3 min timeout to avoid mutex holder crash  
    if (memcache.add(key_mutex, 3 * 60 * 1000) == true) {  
        value = db.get(key);  
        memcache.set(key, value);  
        memcache.delete(key_mutex);  
    } else {  
        sleep(50);  
        retry();  
    }  
}
```

**缓存雪崩解决方案**:

可以在原有的失效时间基础上增加一个随机值，比如1-5分钟随机，这样每一个缓存的过期时间的重复率就会降低，就很难引发集体失效的事件

## 9.redis怎么保持和mysql数据的双写一致性

## 10.redis集群模式主节点宕机,分片算法怎么重新分片

## 11.Redis单线程为什么也这么快

https://blog.csdn.net/xp_xpxp/article/details/100999825

https://www.cnblogs.com/starcrm/p/12984124.html

单线程只是针对redis中的模块来说 比如 接受请求和响应是单线程，处理事件也是单线程 。但是线程不是同一个

![img](https://upload-images.jianshu.io/upload_images/10378860-f024bea7843b5e07.png?imageMogr2/auto-orient/strip|imageView2/2/w/507/format/webp)

1.Redis大部分操作在内存上完成

2.采用高效的数据结构,哈希表和跳表

3.采用多路复用的机制

```
在 Redis 只运行单线程的情况下，该机制允许内核中，同时存在多个监听套接字和已连接套接字。内核会一直监听这些套接字上的连接请求或数据请求。一旦有请求到达，就会交给 Redis 线程处理，这就实现了一个 Redis 线程处理多个 IO 流的效果。

Redis 网络框架调用 epoll 机制，让内核监听这些套接字。此时，Redis 线程不会阻塞在某一个特定的监听或已连接套接字上，也就是说，不会阻塞在某一个特定的客户端请求处理上。正因为此，Redis 可以同时和多个客户端连接并处理请求，从而提升并发性
```

## 12.Redis双写一致性策略

https://zhuanlan.zhihu.com/p/59167071

- (1)先更新数据库,再更新缓存

  这套方案被普遍反对

  **原因一(线程安全角度)** 同时有请求A和请求B进行更新操作,会出现

  - (1)线程A更新数据库

  - (2)线程B更新数据库

  - (3)线程B更新缓存

  - (4)线程A更新缓存

  请求A由于网络等因素导致缓存更新比B慢了,导致缓存里面出现脏数据

  **原因二(业务场景考虑)** 有如下两点

  - (1)如果是写数据库场景比较多,读数据库场景比较少的场景,这种方案会导致数据还没读到,缓存频繁被更新,浪费性能

  - (2)如果写入数据库的值,并不是直接写入缓存,要经过一系列复杂的计算在写入缓存,每次写入数据库,都再次写入缓存，很浪费性能

- (2)先删除缓存,再更新数据库

  该方案会导致不一致的原因是：同时有一个请求A进行更新操作，另一个请求B进行查询操作,会出现如下情况

  (1)请求A进行写操作,删除缓存

  (2)请求B查询发现缓存不存在

  (3)请求B去数据库查询得到旧值

  (4)请求B将旧值写入缓存

  (5)请求A将新值写入数据库,就会导致redis和数据库的这条数据不一致

  **采用延时双删的策略:**

  ```
  public void write(String key,Object data){
          redis.delKey(key);
          db.updateData(data);
          Thread.sleep(1000);
          redis.delKey(key);
      }
  ```

  转化为中文就是

  - 1.先删除缓存
  - 2.再删除数据库
  - 3.休眠1秒,在删除缓存

  **1秒如何确定的,具体休眠多久?**

  要根据业务评估读数据业务逻辑的耗时,然后写数据后的休眠时间在该耗时基础上加几百ms即可,确保读请求结束,写请求可以删除读请求造成的缓存脏数据

  **如果使用mysql的读写分离架构怎么办?**

  这种情况下,造成数据不一致原因还是两个请求，一个A更新,一个请求B进行查询

  - 1.请求A进行写操作,删除缓存
  - 2.请求A将数据写入数据库
  - 3.请求B查询缓存发现,缓存没有值
  - 4.请求B去从库查询,这时候还没有完成主从同步,因此查询到的是旧值
  - 5.请求B将旧值写入缓存
  - 6.数据库完成主从同步,从库变为新值造成了数据不一致,

  **解决:**

  使用延时双删,只是睡眠时间修改为在主从同步的耗时基础上加几百ms

  **采用这种同步策略,吞吐量降低怎么办:**

  把二次删除作为异步,自己起一个线程,异步删除,写的请求不用沉睡一段时间再会返回,这么做加大吞吐量

  **第二次删除删除失败怎么办:**

  - （1）请求A进行写操作，删除缓存
  - （2）请求B查询发现缓存不存在
  - （3）请求B去数据库查询得到旧值
  - （4）请求B将旧值写入缓存
  - （5）请求A将新值写入数据库
  - （6）请求A试图去删除请求B写入对缓存值，结果失败了。 ok,这也就是说。如果第二次删除缓存失败，会再次出现缓存和数据库不一致的问题。 **如何解决呢？** 具体解决方案就是先更新数据库,再删除缓存

  

- (3)先更新数据库,再删除缓存

  假设有两个请求,请求A查询,请求B更新,会有如下情况

  1.缓存刚好失效

  2.请求A查询数据库,得到一个旧值

  3.请求B将新值写入数据库

  4.请求B删除缓存

  5.请求A将查到的旧值写入缓存

发生这种情况要写数据库操作比查询短才会发生,所以一般不会出现,如果非要解决可以用延时删除策略,保证读请求完成后,再进行一次删除操作

**是否还有其他不一致造成的原因**

有,是(2)和(3)都存在的一个问题,如果第二次删除失败怎么办

**解决:两套方案,提供重试机制**

**方案一:** 使用消息队列

如果删除缓存失败,则将缓存的key放入消息队列,队列自己消费消息,获得需要删除的key,持续删除到删除成功为止,该方案对业务线代码造成比较大的侵入

**方案二**.使用订阅程序去订阅数据库的binlog

操作数据库后，数据库会将操作信息写入binlog中,可以使用订阅程序订阅数据库binlog，取出所需要的数据以及key,尝试删除缓存操作,发现删除失败则将消息发送至消息队列,队列不断删除缓存直到成功



# 十.MQ技术

https://www.jianshu.com/p/533ef1924fb5

## 1.如果mq服务器端宕机后,消息如何保证不丢失

```
使用持久化机制,把消息持久化到磁盘
```

## 2.mq如何保证消息不丢失

(1)生产者角色

```
1.confirm机制
每次写消息都会分配一个唯一id，写入mq服务器后,服务器会回传一个ack消息
(1)普通模式
每发送一条消息,调用waitForConfirms方法，等待服务端返回
(2)批量confirm模式,每发送一批消息,调用waitForConfirms方法,等待服务端返回
(3)异步confirm模式,提供一个回调方法,服务端confirm了一条或多条消息后客户端回调这个方法(观察者模式)
2.事务机制
就是begin 提交，回滚

两者不同:
事务机制是同步的,提交一个事务之后会阻塞,但是confirm机制异步,发送消息后可以接着发下个消息,rabbitmq会回调告知是否成功
```

(2)消费者角色

```
ack机制,首先得关闭rabbitmq的自动ack,消费者消费成功手动通知mq服务器去删除该消息,kafka不会删除缓存消息
```

(3)Mq服务器端

```
默认情况下都会对队列中的消息实现持久化,持久化数据到硬盘,有两个步骤,
1.创建queue的时候将其设置为持久化的,可以保证rabbitmq持久化queue的元数据，但不会持久化queue里面的数据
2.发送消息的时候将消息deliveryMode设置为2,这样消息就会被设置为持久化方法,此时rabbitmq就会将消息持久化到磁盘
```



## 3.MQ如何避免消息堆积

```
1.采用集群部署,多个消费者同时消费
2.消费者批量获取消息
```

## 4.传统http请求弊端

```
1.Http请求在高并发情况下,客户端大量发送的请求到达服务器有可能导致服务区请求堆积
2.tomcat服务器处理每个请求都有自己独立线程,如果超过最大线程数会将请求缓存到队列,如果请求堆积过多,会导致tomcat服务器崩溃
```

## 5.RabbitMq死信队列

(1)产生背景

```
也称为备胎队列,消息中间件因为某种原因拒收该消息后,可以转移到死信队列中存放,死信队列也可以有交换机和路由key等
```

(2)产生死信队列的原因

![image-20210506232019398](C:\Users\hasee\AppData\Roaming\Typora\typora-user-images\image-20210506232019398.png)

```
1.消息投递到Mq中存放,消息已经过期
2.队列达到最大的长度(队列容器已经满了)
3.消费者消费多次消息失败,就会转移到死信队列中
```

(3)死信队列应用场景

```
1.订单超时的设计，比如30分钟订单超时
A.Redis过期key
B.死信延迟队列实现
```

## 6.RabbitMq消息幂等问题

(1)当消费者处理执行业务代码时,如果抛出异常情况,会触发RabbitMq自动重试机制，mq服务器重新发送消息给消费者

(2)重试过程中,有可能会引发消费者重复消费问题,那什么情况下要使用，什么情况不使用

```
A.消费者获取消息后调用第三方接口，失败的时候要重试
B.消费者获取消息后,因为代码问题抛出异常,不需要重试
```

(3）如何保证消费者不重复消费

```
1.使用全局id，生产者发送全局id，消费者第一次获得放入redis(SetNX)或数据库(唯一主键约束)，第二次进来去数据库或redis拿，有的话则是第二次,不重复消费
```

## 7.Mq的缺点

- 系统可用性低

```
系统外部依赖多,一旦Mq服务器宕机，会对业务产生影响，需要考虑如何保证高可用
```

- 系统复杂度高

  ```
  异步调用会引入其他的问题比如保证消费者不丢失数据,不会重复调用,怎么保证消息按顺序执行
  ```

  

- 有消息一致性问题

```
A系统处理完业务,通过mq发送消息给B和C，如果B系统处理成功C系统处理失败的情况,需要考虑消息一致性问题
```

## 8.rocketMq消息是不是有序的

# 十一.计算机原理

## 1.tcp和udp区别

```
1.tcp需要进行三次握手建立连接，有连接延时，实时性差,易受攻击,udp不需要,直接发送报文,实时性强，不易受攻击
2.tcp提供超时重连，udp无超时重连
3.tcp只能一对一通信,udp支持一对一，一对多,多对多交互
4、TCP面向字节流，实际上是TCP把数据看成一连串无结构的字节流;UDP是面向报文的
5.TCP的逻辑通信信道是全双工的可靠信道，UDP则是不可靠信道
6.TCP首部开销20字节;UDP的首部开销小，只有8个字节
```



## 2.https的安全性如何保证的

核心: 数据传输使用对称加密,速度快,公钥传输使用非对称加密(ca证书的公钥和私钥)

https://baijiahao.baidu.com/s?id=1614286488938715506&wfr=spider&for=pc

## 3.虚拟内存和物理内存

## 4.正向代理和反向代理

- 正向代理用于帮助内网客户端访问外网

  作用:

     (1)访问原来无法访问的外网资源,如谷歌

     (2)可以做缓存,加速访问资源

     (3)对客户端授权,上网进行认证

     (4)记录用户访问记录,对外隐藏用户的信息

  ![img](data:image/png;base64,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)

- 反向代理将外网客户端请求转发到内网

  作用:

     (1)保证内网安全,阻止web攻击，通常将反向的代理作     为公网访问地址,Web服务器是内网

     (2)复杂均衡,通过反向代理服务器优化网站负载

  ![img](data:image/png;base64,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)

总结:

- 正向代理是客户端代理,代理客户端,服务端不知道实际发起的请求的客户端
- 反向代理是服务端代理,代理服务端,客户端不知道实际提供服务的服务端

![image-20210517232457674](C:\Users\hasee\AppData\Roaming\Typora\typora-user-images\image-20210517232457674.png)

## 5.tcp的三次握手和四次挥手

- 三次握手

  ![img](https://img-blog.csdn.net/20170104214009596?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvd2h1c2xlaQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

  首先客户端发送连接请求SYN报文以测试自己发送能力是否正常,服务端接收报文回复ACK报文,并给连接分配资源,客户端接收到ACK报文后,确认了自己发送消息正常,也向服务端发送ACK报文,并分配资源,这样服务端也能确认自己发送消息正常,TCP就建立了

  **是否可以二次握手?**

  经过上面的过程，如果是二次握手,客户端知道自己发送能力正常,但是服务端不能确定自己发送能力是否正常

  **SYN泛红攻击?**

  SYN泛红攻击原理就是恶意终端故意不发送第三个握手包,操作系统完成第二次握手包后会分配资源给这个半连接,同时将半连接放入半连接队列,边连接过多会导致服务器队列塞满,正常用户请求得不到响应

- 四次挥手

![image-20210520000051060](C:\Users\hasee\AppData\Roaming\Typora\typora-user-images\image-20210520000051060.png)

中断连接可以是客户端,也可以是服务端

```
假设客户端发起中断连接请求,也就是发送FIN报文，告诉服务端我没有数据要给你了,如果有数据没有发送完成,先不用急着关闭socket，可以继续发送数据。然后服务端发送ACK报文,告诉客户端,还没准备好,请继续等待我的消息,这时候客户端进入FIN_WAIT状态,继续等待服务端的FIN报文.当服务端确定数据已经发送完成,则向客户端发送FIN报文,告诉客户端,数据发送完毕,已经准备好关闭连接,客户端收到FIN报文后，就知道可以关闭连接,会再发送一次ACK报文给服务端然后进入TIME_WAIT状态,如果服务端没收到ACK报文则可以重传,服务端收到ACK报文后,就可以断开连接,客户端等待2MSL(最大报文段生成时间)后依然没有收到回复，证明server端正常关闭,客户端也关闭连接
【注意】 在TIME_WAIT状态中，如果TCP client端最后一次发送的ACK丢失了，它将重新发送。TIME_WAIT状态中所需要的时间是依赖于实现方法的。典型的值为30秒、1分钟和2分钟。等待之后连接正式关闭，并且所有的资源(包括端口号)都被释放。
```

## Tcp为什么要四次挥手？

因为Tcp是全双工通信的,断开的时候两边都需要确认对方是否还有数据要发送,所以要有两次发送fin报文和两次发送ack信号以确认对方数据发送完毕

## 6.怎么保持tpc可靠传输

1.确认应答机制&序列号

```
tcp给每个字节数据都进行了编号,即为序列号.每一个ACK都带对应的确认序列号,告诉发送者,已经收到了哪些数据,下次从哪些开始发
```

2.超时重传&序列号

```
主机A发送数据给B后，因为网络拥堵,数据无法到达B,如果主机A在特定时间没有收到B的确认应答,就重发,B这边根据A发来数据中的序列号来判断哪些是重复的包,然后去除重复的包
```

3.拥塞控制

每次发送数据包的时候,将拥塞窗口和接收端主机反馈的窗口大小做比较,取较小的值作为实际发送的窗口

4.滑动窗口机制



https://blog.csdn.net/sifanchao/article/details/82285018

# 十二.业务算法场景

https://blog.csdn.net/qq_33666011/article/details/104994641?utm_medium=distribute.pc_relevant.none-task-blog-baidujs_title-0&spm=1001.2101.3001.4242

## 1.给定a,b两个文件,存放50亿个url，M诶个url64个字节,内存限制4G，找出交集的url

**主题思路:**

分治+hash

**实现步骤:**

遍历文件A，对每个url使用`hash(url) % 1000`，根据所得的取值将url存储到1000个小文件中（a1,a2,…,a1000）（根据内存大小设定hash函数） 遍历文件B，使用同样的hash函数将B中的url存储到1000个小文件中（b1,b2,…,b1000）,也会有哈希冲突情况,所以还要继续处理，读取文件a1,建立一个hash表,再读取文件b1，遍历url，如果url在hash表中出现,说明两文件共有，存入结果

## 2.海量日志数据，提取出某日访问百度次数最多的那个IP。

则是： “首先是这一天，并且是访问百度的日志中的IP取出来，逐个写入到一个大文件中。注意到IP是32位的，最多有个2^32个IP。同样可以采用映射的方法，比如Hash(IP)%1000，把整个大文件映射为1000个小文件，再找出每个小文中出现频率最大的IP（可以采用hash_map对那1000个文件中的所有IP进行频率统计，然后依次找出各个文件中频率最大的那个IP）及相应的频率。然后再在这1000个最大的IP中，找出那个频率最大的IP，即为所求。

```
Hash取模是一种等价映射，不会存在同一个元素分散到不同小文件中的情况，即这里采用的是mod1000算法，那么相同的IP在hash取模后，只可能落在同一个文件中，不可能被分散的。因为如果两个IP相等，那么经过Hash(IP)之后的哈希值是相同的，将此哈希值取模（如模1000），必定仍然相等。
```

## 2.寻找热门查询,300万个查询字符串中统计最热门10个查询

- 原题：搜索引擎会通过日志文件把用户每次检索使用的所有检索串都记录下来，每个查询串的长度为1-255字节。假设目前有一千万个记录（这些查询串的重复度比较高，虽然总数是1千万，但如果除去重复后，不超过3百万个。一个查询串的重复度越高，说明查询它的用户越多，也就是越热门），请你统计最热门的10个查询串，要求使用的内存不能超过1G。

## 3.请求怎么通过短链接重定向到长链接，怎么实现

## 4,微信抢红包系统怎么设计

## 5.商品秒杀系统如何设计

## 6.Redis和zookeeper实现分布式锁

# 十三.Nginx技术

## 一.下载安装

http://nginx.org/en/download.html下载地址

windows下载解压后执行exe文件启动即可,cmd下启动

linux下解压后.configuer然后make &&make install安装

whereis nignx导向到安装的目录进入sbin下执行./nignx

## 二.nginx常用命令

cd到nignx安装目录下

```
./niginx 启动
./nginx -s stop 停止
./nginx -s quit 安全退出
./nginx -s reload 重新加载配置文件
ps aux | grep nginx 查看nginx进程
```

## 三.nginx.conf配置文件内容

全局配置,events,http

1.全局配置:

```
worker_processes:工作进程数
   默认是1
   调整:一般调整为cpus核心数
```

2.events:

```
events中有个worker_connections 1024,代表单个worker进程最大的连接数
```

3.http

```
http下有两个重要的配置
server和upstream,upstream用来做负载均衡
server配置当前的请求信息
```

配置例子:

    #user  nobody;
    worker_processes  1;
    
    #error_log  logs/error.log;
    #error_log  logs/error.log  notice;
    #error_log  logs/error.log  info;
    
    #pid        logs/nginx.pid;


​    
​    events {
​        worker_connections  1024;
​    }


​    
​    http {
​        include       mime.types;
​        default_type  application/octet-stream;
​    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
​    #                  '$status $body_bytes_sent "$http_referer" '
​    #                  '"$http_user_agent" "$http_x_forwarded_for"';
​    
​    #access_log  logs/access.log  main;
​    
​    sendfile        on;
​    #tcp_nopush     on;
​    
    #keepalive_timeout  0;
    keepalive_timeout  65;
    
    #gzip  on;
    
    upstream gavin {
         server 127.0.0.1:8080 weight=1;
    	 server 127.0.0.1:8080 weight=1;
    }
    
    server {
        listen       80;
        server_name  localhost;
    
        location / {
            root   html;
            index  index.html index.htm;
    	    proxy_pass http://gavin
        }
    
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
     }
    }
location中/的意思就是跟路径代理到proxy_pass中的对象gavin

gavin中有两个被代理的地址用权重区分

# 十四.Mybatis技术

## 一.mybatis一对一,一对多,多对多实现

https://blog.csdn.net/qq_32659773/article/details/106781327

# 十五.Linux技术

## 一.删除某个时间前的所有文件命令

```
find 目录 -mtime +1(天数) -name "*.log" -exec -rm -rf {} \; 
```

## 二.查看代码项目文件总代码行数

```
find . -name "*.py" -o -name "*.sh" | xargs cat | wc -l
```

