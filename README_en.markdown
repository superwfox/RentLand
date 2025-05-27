# RentLand - Paper Land Management Plugin

RentLand is a land management plugin for Minecraft Paper servers, integrating land leasing, permission management, and a notification system. It connects to a WebSocket-based bot (e.g., a QQ bot using the OneBot protocol) to send notifications. Players can lease land using experience levels, manage permissions, and receive reminders for lease expirations.

## Project Overview

- **Project Name**: RentLand  
- **Project Type**: Paper Server Plugin  
- **Main Features**:  
  - Land leasing with experience level payments and customizable lease durations.  
  - Permission management for granting access to other players.  
  - Notification system via WebSocket for lease expirations and permission requests.  

## Features

- **Land Leasing**:  
  - Players define land boundaries by left-clicking blocks and pay with experience levels.  
  - Lease duration is set in days and can be modified via a property deed.  
- **Permission Management**:  
  - Land owners can grant or revoke access to other players.  
  - Permissions can be managed via in-game chat or bot responses.  
- **Notification System**:  
  - Sends notifications for lease expirations and permission requests via a WebSocket-connected bot.  
  - Supports group and private messages for communication.  
- **Property Deed**:  
  - Uses a writable book (`WRITABLE_BOOK`) as a property deed to store land details and permissions.  
  - Players can edit the deed to update land names, lease durations, or remove permissions.  
- **Configuration**:  
  - Customizable world name, WebSocket port, QQ group ID, and bot name via a configuration file.  

## Technical Implementation

- **Programming Language**: Java  
- **Dependencies**:  
  - Paper API  
  - OneBot (WebSocket client for bot communication)  
- **File Management**:  
  - Stores land data in a CSV file (`land.csv`).  
  - Uses a YAML file (`config.yml`) for configuration settings.  
- **Event Listeners**:  
  - Handles player interactions, chat, and movement events to manage land-related logic.  
- **Scheduled Tasks**:  
  - Periodically checks lease durations and sends expiration reminders.  

## Usage Instructions

### Installation
1. Place the plugin's JAR file in the `plugins` directory of your Paper server.  
2. Start the server to generate the default configuration file (`config.yml`) and land data file (`land.csv`).

### Configuration
Edit the `plugins/RentLand/config.yml` file to configure the following settings:
```yaml
# The name of the world managed by the plugin (supports only one world)
WorldName: "world"

# WebSocket connection port (default: 3001)
port: 3001

# QQ group ID
QQGroup: "123456789"

# Bot name
botName: "Bot"
```
Save the file and restart the server to apply changes.

### Commands
- `/book`: Obtain a property deed (requires OP permission).  

### Operations
1. **Leasing Land**:  
   - Use a property deed (writable book) and left-click blocks to define land boundaries.  
   - Enter the desired lease duration (in weeks) via chat to complete the lease, consuming experience levels.  
2. **Managing Permissions**:  
   - Edit the property deed to add or remove player permissions.  
   - Respond to permission requests via in-game chat ("是" for yes) or bot messages ("允许" for allow).  
3. **Viewing Land Details**:  
   - Right-click the property deed to view land information and permissions.  

## Code Structure

- **`RentLand.java`**: Main plugin class, handles initialization, event registration, and WebSocket connection.  
- **`FileManager.java`**: Manages reading and writing to CSV and YAML files.  
- **`CommandManager.java`**: Handles the `/book` command to generate property deeds.  
- **`BookController.java`**: Manages property deed editing and interaction logic.  
- **`LandNotice.java`**: Handles notifications for land entry and permission requests.  
- **`LandTimer.java`**: Schedules tasks to check lease durations and send expiration reminders.  
- **`PurChaseListener.java`**: Processes land leasing interactions and boundary selection.  
- **`PlayerChatListener.java`**: Handles player chat input for lease completion and ticket generation.  
- **`OneBotClient.java`**: Implements WebSocket communication with the bot.  
- **`PlayerMoveDetector.java`**: Detects player movement to check for entry into restricted lands.  

## Notes

- **Permissions**:  
  - The `/book` command and certain features require OP permission.  
- **Configuration**:  
  - Ensure `config.yml` is properly set up with the correct world name and WebSocket port to avoid issues.  
- **Bot Setup**:  
  - A bot supporting the OneBot protocol must be running, with the WebSocket address correctly configured (e.g., `ws://127.0.0.1:3001`).  
- **Server Restart**:  
  - Configuration changes require a server restart to take effect.  

## Contribution and Feedback

- **Issue Reporting**: Report bugs or issues on the [GitHub Issues](https://github.com/yourusername/RentLand/issues) page.  
- **Code Contributions**: Pull Requests are welcome for plugin enhancements.  
- **Documentation**: This README provides detailed usage and configuration instructions; contributions to improve it are appreciated.  

## License

This project is licensed under the [MIT License](LICENSE) (assumed; adjust as needed). See the LICENSE file for details.