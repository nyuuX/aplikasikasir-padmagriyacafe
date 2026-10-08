YOU WILL NEED DOCKER DESKTOP TO RUN THIS PROGRAM!!!

 ### Prerequisites & Docker Installation

1. **Download Docker Desktop**
Visit [docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop) and click the download button for Windows. Select the **AMD64** version for standard Intel/AMD processors, or **ARM64** if your device uses a Snapdragon/ARM processor.
2. **Run the Installer**
Open the `Docker Desktop Installer.exe` file. Ensure the **Use WSL 2 instead of Hyper-V** option remains checked (default setting), then click **OK** and wait for the installation to complete.
3. **Restart Your Device**
If the system prompts you to restart or log out after the installation, please follow the instructions. This step is necessary to properly configure the WSL 2 components on Windows.
4. **Initial Docker Setup**
Open Docker Desktop from the Start Menu. Click **Accept** on the Terms of Service page. If a login screen appears, you can click **Skip** or continue without an account (an account is not required to run this project).
5. **Wait for "Running" Status**
Check the bottom left corner of the Docker Desktop window. Wait about 1–2 minutes during this initial startup until a green indicator or the words **Engine running** appear.
6. **Verify and Run the Application**
Open **Command Prompt (CMD)** and type the following command to make sure Docker is ready:
```bash
docker --version

```


If the screen displays the Docker version number, the installation was successful. Next, open the `PadmagriyaCafe` folder and double-click the `JALANKAN.bat` file to start the application.
 
