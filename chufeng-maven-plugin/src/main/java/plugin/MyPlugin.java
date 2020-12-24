package plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "hello")
public class MyPlugin extends AbstractMojo {
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    System.err.println("hello my plugin......");
  }
}
