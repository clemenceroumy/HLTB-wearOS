<div align="center"><img align="center" src="./documentation/logo.webp" alt="application logo"/></div>
<h1 align="center">HLTB WearOS</h1>
<p align="center">HowLongToBeat but for wearOS</p>

<br/>

Based on the **HLTB website** https://howlongtobeat.com/

**Next** : Optimize code to improve battery saving.

HowLongToBeat on WearOS. 
- Retrieve your HLTB games status by category. 
- Select a game to start a stopwatch and save your timed session at the end of it.
- See an history of your sessions.
- If an error occurs while sending a session to HLTB, a log is saved locally and you will be able to try sending it later (or upload it manually to HLTB and remove it from the logs).

## Installation

The app is not available through Google Play Store (at least for now).
Here's the steps to follow to get it : 

1. Head over to the release page and download the latest release apks. 
There's one for the actual wearOS app and an another one for the phone app (needed to logged into your HLTB account, it is mandatory to use the wearOS app).
2. Install the phone app `mobile-HLTBwearOS.apk` on your phone. Once installed, the application is purposely not visible on your phone app list. You can find it in the Settings apps list of your phone, searching for "HLTBWearOS".
3. Install the wearOS app `watch-HLTBwearOS.apk` on your watch. Installing an .apk on a wearOS device can be a bit tricky. I personally use the [Wear OS Tools for windows](https://forum.xda-developers.com/attachments/wearos-tools-v10-rar.5927083/) tools to do so.
4. Have fun beating your games !😁


## Screenshots

### Login

![Login](./documentation/login_example.gif)

### Display games by categories

![HomePlayingCategory](./documentation/home_playing.png)
![HomeBacklogCategory](./documentation/home_backlog.png)

### Launch a gaming session

![TimedSession](./documentation/saving_time.gif)

### Logs of sessions

![Logs](./documentation/logs.gif)