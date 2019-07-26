#!/bin/sh
################################################

#定义路径变量
export PROJ_PATH=`pwd`
export TOMCAT_APP_PATH=/developer/apache-tomcat-7.0.90

#杀掉进程
killTomcat()
{
    pid=`ps -ef|grep tomcat|grep -v grep|awk '{print $2}'`
    echo "tomcat Id list :$pid"
    if [ "$pid" = "" ]
    then
        echo "no tomcat pid alive"
    else
        kill -9 $pid
    fi
}

cd $PROJ_PATH
mvn clean install

killTomcat

INSTALLDIR=$TOMCAT_APP_PATH/webapps

#push code to server and start server
cd $INSTALLDIR

#删除根目录工程
rm -rf ROOT
rm -rf ROOT.war
rm -rf gwsystem
rm -f  gwsystem.war

#copy项目到Tomcat路径下
cp -ar $PROJ_PATH/robot/target/*.war $INSTALLDIR

#项目重命名
mv *.war ROOT.war

#startup Tomcat
sh $TOMCAT_APP_PATH/bin/startup.sh
RESULT=`netstat -lntup|grep 8081|wc -l`
if [ $RESULT -eq 1 ];then
   echo "tomcat start sucessfull"
else
   sh $TOMCAT_APP_PATH/bin/startup.sh
fi

sleep 1s