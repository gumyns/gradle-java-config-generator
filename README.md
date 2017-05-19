# gradle-java-config-generator
Main purpose is to mitigate feature from Android plugin which provides BuildConfig fields in gradle, but it's far from flexibility. 
This one is more flexible, and isn't tied to Android plugin, so you can use it also in java projects.

## Usage
in build.gradle
```
buildscript {

    repositories {
        flatDir dirs:  "plugin/"
    }
    dependencies {
        classpath "pl.gumyns:java-config-generator:0.1"
    }
}

...

apply plugin: 'pl.gumyns.java-config-generator'
configGenerator {
    packageName("com.app.generated")
    file("./config.json")
}

```

in json file:

```
[
  {
    "name":"Mail",
    "variables": [
      {"type":"String", "name": "value1", "value":"Ekhem"},
      {"type":"int", "name": "value2", "value":"2"}
    ]
  },
  {
    "name":"SomeClass",
    "variables": [
      {"type":"boolean", "name": "value3", "value":"false"},
      {"type":"double", "name": "value7", "value":"3.14"},
      {"type":"double", "name": "value8", "values":["3.14", "42."]},
      {"type":"java.wut.Wow", "name": "wow", "values":["new Wow(3.14)", "new Wow(42.)"]}
    ]
  }
]
```

It will generate 2 classes:
```
public final class Mail {
  public static final String value1 = "Ekhem";
  public static final int value2 = 2;
}
```
```
import java.wut.Wow;

public final class SomeClass {
  public static final boolean value3 = false;
  public static final double value7 = 3.14;
  public static final double[] value8 = new double[]{3.14, 42.};
  public static final Wow[] wow = new Wow[]{new Wow(3.14), new Wow(42.)};
}
```
