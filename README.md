# 基于Netty IM sdk 
1. TCP 
2. 集成
```
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
            //主lib
	        implementation 'com.github.NBXXF:xxf_netty:1.0.2'
	        implementation 'com.google.code.gson:gson:2.8.6'
	        implementation 'io.reactivex.rxjava3:rxjava:3.0.8'
	        implementation 'com.github.NBXXF.xxf_database:xxf_objectbox:1.0.7'
	}
Share this release:
```
