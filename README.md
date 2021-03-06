# gradle-java-config-generator
Main purpose is to mitigate feature from Android plugin which provides BuildConfig fields in gradle, but it's far from flexibility. 
This one is more flexible, and isn't tied to Android plugin, so you can use it also in java projects.


## Usage

Put plugin file to root/plugins or wherever you want.

in build.gradle
```
buildscript {

    repositories {
        flatDir dirs:  "plugin/" // put directory where plugin is
    }
    dependencies {
        classpath "pl.gumyns:java-config-generator:0.3"
    }
}

...

apply plugin: 'pl.gumyns.java-config-generator'
configGenerator {
    packageName("com.app.generated")
    file("config.yaml")
   // file("config.yaml", "config2.yaml") // you can also use multiple files
}
```


in yaml file:
```
- class: Mail
  vars:
  - boolean enabled true
  - String mail some@email.com
- class: Arrays
  vars:
  - String texts [ok;cancel]
  - java.wut.Wow texts [new Wow("Hello");new Wow("World")]
```

It will generate 2 classes:
```
import java.lang.String;

public final class Mail {
  public static final boolean enabled = true;
  public static final String mail = "some@email.com";
}
```
```
import java.lang.String;
import java.wut.Wow;

public final class Arrays {
  public static final String[] texts = new String[]{ "ok", "cancel" };
  public static final Wow[] texts = new Wow[]{ new Wow("Hello"), new Wow("World") };
}
```

If you need to generate something per build type, or Android flavor, you can use this:
```
apply plugin: 'pl.gumyns.java-config-generator'

ext.extraConfigFiles = new LinkedList<String>()
configGenerator {
    packageName("com.app.generated")
    file("./config.yaml")
}
```

And then, anywhere in the project:
```
extraConfigFiles.add("./extraConfig.yaml")
```

