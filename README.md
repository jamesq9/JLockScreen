# JLockScreen
Do you have an old pendrive just lying around and want to use it to lock and unlcok your machine instead of the traditional way? if yes then I present to you a Java Application which does exactly that.

## Installation
The unlock mechanism works by using an RSA public and private key. You need to place the Private key on the pendrive and its name should be '.junlock' and the public key in the '.jlock' file.

you can generate both the keys by using the below command:

```bash
java -jar JLockScreen.jar gen
```


 ## Usage
 After generating both the lock and unlock files, place the unlock file on the root directory of the pendrive, you can hide it if you like (Note: do not place it in any sub folders).  place the unlock file on the same directory as the jar file. double click on the jar file to run it. Remove the pendrive and the program will lock your screen with a Custom annoying JFrame that won't leave your screens. insert the pendrive back into the machine and the Lockscreen will disappear. To kill the Process use the task manager and end the task of the Java Application.
