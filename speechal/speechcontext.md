语音服务功能
=======
* 定义统一的语音服务抽象层接口
* 封装第三方语音引擎
* 命令词分发

语音服务架构
=======
一、 语音服务\总线\应用关系示意图

![Aaron Swartz](https://github.com/marklogg/images/blob/master/model1.png?raw=true)

说明:

* Robot Bus Service --总线服务
    
* Robot App  -- 机器人端应用

* SAL -- 语音服务抽象层(Speech Abstract Layer)

      定义语音服务进程与总线服务进程通讯的语音协议；定义优必选多个机器人产品通用的语音环境接口，
      各第三方语音引擎接入需满足规定格式。
      
     
* IfytekSpeech -- 封装科大讯飞语音引擎的语音模块

      按SAL给定的接口形式封装科大讯飞语音sdk的语音module。
      
* NaunceSpeech -- 封装nuance语音引擎的语音模块

      按SAL给定的接口形式封装科大讯飞语音sdk的语音module。
      
二、语音服务抽象层结构
    
      
      

      
      