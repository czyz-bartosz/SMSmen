# SMSmen - SMS API

The goal of this project is to make SMS API to send SMS via your own android phone. It can be used for SMS auth codes, newsletters. It contains server and client. The server was builded  in node js with express, the client is android app with kotlin.

## How it works

The server maintain queue of sms to be send. The client app makes get requests to the server and sends message. To add message to queue make post request to the server's endpoint */api/sms* with body:

```
{
    "phoneNumber": "123123123",
    "message": "test message"
}
```

## How to set up

### Server

1. Download files or clone git repo.
2. Change folder to *SmsServer* and run command `npm install` to install dependiencies
3. Copy *.env.template* file and rename it as *.env*
4. Edit *.env* file to set PORT of your server
5. Run command `npm start` in *SmsServer* folder to start server
6. Open port in firewall

### Client

1. Download and install *SmsClient.apk* on Android phone.
2. Open app.
3. Give app permission by clicking button on the app's main screen.
4. Fill in server ip address with port and click save.
5. You can close the app.

 :exclamation: **By default, Android will check to make sure you haven't sent more than 30 SMS messages to anyone within a span of 30 minutes. However, we can manually change both of these values (the number of messages and the time frame) so that we aren't bothered by Android's default SMS limit restrictions.**

1. [Install ADB](https://www.xda-developers.com/install-adb-windows-macos-linux/)
2. [Change Android's SMS Limit](https://www.xda-developers.com/change-sms-limit-android/)
3. Restart phone

## To do list

- [ ] Add option to store queue in database
- [ ] Add API authentication
- [ ] Add field to change delay of client request to server