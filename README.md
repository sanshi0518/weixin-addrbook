weixin-addrbook
===============

A back-end address book service for weixin public platform


## 微信公众平台开发者文档

[微信公众平台开发者文档](http://mp.weixin.qq.com/wiki/index.php)



## Deploy

[Sina App Engine](http://sae.sina.com.cn/)


## Usage

Support two query modes：

	full-name
	short-pinyin-name


## Launch

Launch the application by issuing one of the following commands:

    lein run [host <host>] [port <port>]
    
You can generate a standalone jar and run it:

	lein uberjar
	java -jar target/weixin-addrbook-0.1.0-SNAPSHOT-standalone.jar

You can also generate a war to deploy on a server like Tomcat, Jboss...

	lein ring uberwar
	
For deveplopment:

	lein ring server-headless
	

## License

Copyright © 2014 wanglei

Distributed under the Eclipse Public License, the same as Clojure.

