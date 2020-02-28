#!/bin/bash

# 请注意
# 本脚本的作用是把本项目编译的结果保存到deploy文件夹中
# 1. 把项目数据库文件拷贝到deploy/db
# 2. 编译behave-admin
# 3. 编译behave-all模块，然后拷贝到deploy/behave

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
cd $DIR/../..
LITEMALL_HOME=$PWD
echo "LITEMALL_HOME $LITEMALL_HOME"

# 复制数据库
cat $LITEMALL_HOME/behave-db/sql/behave_schema.sql > $LITEMALL_HOME/deploy/db/behave.sql
cat $LITEMALL_HOME/behave-db/sql/behave_table.sql >> $LITEMALL_HOME/deploy/db/behave.sql
cat $LITEMALL_HOME/behave-db/sql/behave_data.sql >> $LITEMALL_HOME/deploy/db/behave.sql

cd $LITEMALL_HOME/behave-admin
# 安装阿里node镜像工具
npm install -g cnpm --registry=https://registry.npm.taobao.org
# 安装node项目依赖环境
cnpm install
cnpm run build:dep

cd $LITEMALL_HOME
mvn clean package
cp -f $LITEMALL_HOME/behave-all/target/behave-all-*-exec.jar $LITEMALL_HOME/deploy/behave/behave.jar