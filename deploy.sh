#!/usr/bin/env bash
echo "=============进入git项目目录路径=================="
cd /developer/git-repository/mmall_learning

echo "==============git切换分支到"$1"=================="
git checkout $1

echo "==================git fetch====================="
git fetch

echo "==================git pull======================"
git pull

echo "==============编译并跳过单元测试=================="
mvn clean package -Dmaven.test.skip=true -Pdev

echo "===============删除旧的ROOT.war=================="
rm /developer/$2/webapps/ROOT.war

echo "======拷贝编译出来的war包到tomcat下-ROOT.war======"
cp /developer/git-repository/mmall_learning/target/mmall.war /developer/$2/webapps/ROOT.war

echo "===========删除tomcat下旧的ROOT文件夹============"
rm -rf /developer/$2/webapps/ROOT

echo "=================关闭tomcat====================="
/developer/$2/bin/shutdown.bat

echo "================= sleep 10s ===================="
for i in {1..10}
do
    echo $i"s"
    sleep 1s
done

echo "=================启动tomcat====================="
/developer/$2/bin/startup.bat
















