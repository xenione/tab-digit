# Tab Digit

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Tab%20digit-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/4786)  [![Download](https://api.bintray.com/packages/xenione/maven/tab-digit/images/download.svg) ](https://bintray.com/xenione/maven/tab-digit/_latestVersion)

A Flip Clock Libray.Have a look at the demo app available on [google play](https://play.google.com/store/apps/details?id=com.xenione.libs.digit)

![tab-digit](https://cloud.githubusercontent.com/assets/4138527/20869273/258ed640-ba6f-11e6-892c-a5986896134c.gif)

this is what you can do with Tab digit

![clock](https://cloud.githubusercontent.com/assets/4138527/20869514/f503a208-ba73-11e6-800b-802d493e1a86.gif)


Add it on your project:

Gradle:
```java 
compile 'com.xenione.libs:tab-digit:1.0.2'
```

Add tabdigit in your layout, you can set attributes like background or text color, text size and padding through xml as shown.

```java 
 <com.xenione.digit.TabDigit
     xmlns:digit="http://schemas.android.com/apk/res-auto"
        android:id="@+id/tabDigit1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:layout_weight="1"
        digit:backgroundColor="#000000"
        digit:padding="10dp"
        digit:textColor="#ffffff"
        digit:textSize="60dp" />
  ```      

start animation calling start().

```java 
tabDigit1.start();
  ```  
Increase digit every second:

```java 
ViewCompat.postOnAnimationDelayed(tabDigit1, this, 1000);

@Override
    public void run() {
        tabDigit1.start();
        ViewCompat.postOnAnimationDelayed(tabDigit1, this, 1000);
    }
  ```  

# License
-------
    Copyright 2016 Eugeni Josep Senent i Gabriel

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
