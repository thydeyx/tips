#!/bin/sh
# - P $ENV定义了用户自定义的参数
mvn clean -U package -P $ENV -Dmaven.test.skip=true
