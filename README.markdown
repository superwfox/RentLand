# RentLand - Paper 领地管理插件

## [【View in English】](https://github.com/superwfox/RentLand/blob/master/README_en.markdown)

RentLand 是一个专为 Minecraft Paper 服务器设计的领地管理插件，集成了领地租赁、权限管理和通知系统等功能，并通过 WebSocket 连接机器人（如基于 OneBot 协议的 QQ 机器人）实现消息通知。玩家可以通过消耗等级租赁领地、管理权限，并接收到期提醒等通知。

## 项目概述

- **项目名称**: RentLand  
- **项目类型**: Paper 服务器插件  
- **主要功能**:  
  - 领地租赁：玩家通过等级支付租赁费用，租期以天为单位。  
  - 权限管理：领地主人可为其他玩家分配权限。  
  - 通知系统：通过 WebSocket 连接机器人发送领地到期提醒和权限请求通知。  

## 功能特点

- **领地租赁**:  
  - 玩家通过左键点击方块圈定领地范围，消耗等级完成租赁。  
  - 租赁时间以天为单位，可通过地产证调整租期。  
- **权限管理**:  
  - 领地主人可为其他玩家授予进入权限。  
  - 支持通过聊天或机器人回复确认权限请求。  
- **通知系统**:  
  - 通过 WebSocket 连接的机器人发送通知（如领地到期、权限请求）。  
  - 支持群聊和私聊消息提醒。  
- **地产证**:  
  - 使用可编辑书本（`WRITABLE_BOOK`）作为地产证，记录领地信息和权限列表。  
  - 玩家可通过编辑书本修改领地名称、租期或移除权限。  
- **配置文件**:  
  - 支持自定义世界名称、WebSocket 端口、QQ 群号和机器人名称。  

## 技术实现

- **编程语言**: Java  
- **依赖**:  
  - Paper API  
  - OneBot（WebSocket 客户端，用于机器人通信）  
- **文件管理**:  
  - 使用 CSV 文件 (`land.csv`) 存储领地数据。  
  - 使用 YAML 文件 (`config.yml`) 存储配置信息。  
- **事件监听**:  
  - 监听玩家交互、聊天、移动等事件，实现领地管理逻辑。  
- **定时任务**:  
  - 定期检查领地租期并发送到期提醒。  

## 使用方法

### 安装
1. 将插件的 JAR 文件放入 Paper 服务器的 `plugins` 目录。  
2. 启动服务器，插件会自动生成默认配置文件 `config.yml` 和领地数据文件 `land.csv`。

### 配置
编辑 `plugins/RentLand/config.yml` 文件，配置以下内容：
```yaml
# 领地插件管理的世界名称（仅支持一个）
WorldName: "world"

# WebSocket 正向连接端口（默认 3001）
port: 3001

# QQ 群号
QQGroup: "123456789"

# 机器人名称
botName: "机器人"
```
保存后重启服务器以应用配置。

### 命令
- `/book`: 获取地产证（需要 OP 权限）。  

### 操作
1. **租赁领地**:  
   - 使用地产证（可编辑书本），左键点击方块圈定领地范围。  
   - 在聊天中输入租赁周数，消耗相应等级完成租赁。  
2. **管理权限**:  
   - 编辑地产证添加或移除玩家权限。  
   - 当其他玩家进入领地时，可通过聊天或机器人回复“允许”授予权限。  
3. **查看领地信息**:  
   - 右键点击地产证查看领地详情和权限列表。  

## 代码结构

- **`RentLand.java`**: 插件主类，负责初始化、事件注册和 WebSocket 连接。  
- **`FileManager.java`**: 处理 CSV 和 YAML 文件的读写操作。  
- **`CommandManager.java`**: 处理 `/book` 命令，生成地产证。  
- **`BookController.java`**: 管理地产证的编辑和交互逻辑。  
- **`LandNotice.java`**: 处理领地进入通知和权限请求逻辑。  
- **`LandTimer.java`**: 定时检查领地租期并发送到期提醒。  
- **`PurChaseListener.java`**: 处理领地租赁的交互逻辑和范围圈定。  
- **`PlayerChatListener.java`**: 处理玩家聊天输入，完成租赁和票据生成。  
- **`OneBotClient.java`**: 实现与机器人的 WebSocket 通信。  
- **`PlayerMoveDetector.java`**: 检测玩家移动，判断是否进入他人领地。  

## 注意事项

- **权限**:  
  - `/book` 命令和部分功能需要 OP 权限。  
- **配置**:  
  - 必须正确配置 `config.yml`，包括世界名称和 WebSocket 端口，否则插件无法正常运行。  
- **机器人**:  
  - 需要运行一个支持 OneBot 协议的机器人，并确保 WebSocket 地址正确（如 `ws://127.0.0.1:3001`）。  
- **服务器重启**:  
  - 修改配置后需重启服务器以生效。  

## 贡献和反馈

- **问题反馈**: 请在 GitHub 的 [Issues](https://github.com/yourusername/RentLand/issues) 页面报告问题。  
- **代码贡献**: 欢迎提交 Pull Requests，参与插件开发。  
- **文档**: 本 README 提供详细的使用说明和配置指南，欢迎补充完善。  

## 许可证

本项目采用 [MIT 许可证](LICENSE)（假设为 MIT，可根据实际情况调整）。详情请参阅 LICENSE 文件。
