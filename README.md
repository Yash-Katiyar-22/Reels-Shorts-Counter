# 🎧 Reels & Shorts Counter

An Android digital wellbeing app that tracks how many Instagram Reels and YouTube Shorts you scroll — and how much time you spend doing it.  
Because sometimes the biggest lie we tell ourselves is “just one more reel”.

---

## ⚠️ Installation Note (Important)

This app is **not published on the Google Play Store**.  
Since it uses Accessibility and Usage Access permissions, Android may apply certain security restrictions while installing and testing the app.

Follow these steps to run the app without issues:

### Step 1: Disable Play Protect

1. Open **Google Play Store**  
2. Tap on your **profile icon** (top-right corner)  
3. Go to **Play Protect**  
4. Tap the **Settings icon**  
5. Turn **Play Protect scanning** OFF  

---

### Step 2: Install the App

- Download and install the APK file normally.

---

### Step 3: Allow Restricted Settings (if required)

If Android shows a restriction warning:

1. Open **Phone Settings**  
2. Go to **Apps**  
3. Tap the **three-dot menu** (top-right)  
4. Select **Show system apps** (if needed)  
5. Find **Reels & Shorts Counter**  
6. Tap the **three-dot menu** inside the app info page  
7. Select **Allow restricted settings**

---

### Step 4: Grant Required Permissions

When you open the app, allow:

- **Usage Access Permission**  
- **Accessibility Service Permission**

---

### ✅ Done!

The app will now run properly and start tracking Reels and Shorts activity.

---

> Note: All data stays on your device. No internet or cloud storage is used.


## 🚀 About

Reels & Shorts Counter runs in the background and monitors short-form video scrolling activity.  
It counts every Reel/Short viewed, tracks time spent, and displays daily stats in a simple dashboard and home-screen widget to help users become aware of their scrolling habits.

---

## ✨ Features

- Detects Instagram Reels and YouTube Shorts  
- Counts number of scrolled short videos  
- Tracks total time spent  
- Daily usage statistics  
- Home-screen widget for quick view  
- Optional daily reminder  
- Works completely offline  

---

## 🛠️ Tech Stack

- Platform: Android  
- Language: Kotlin  
- UI: Jetpack Compose  
- Background Services: Accessibility Service & Usage Stats API  
- Local Storage: Room Database (SQLite)  
- Widget: Android App Widget  

---

## ⚙️ How It Works

The app uses system permissions to detect:
- Which app is currently open  
- When scrolling happens inside Reels or Shorts  

Each scroll increments a counter stored locally.  
The collected data is shown in the app dashboard and widget as daily usage insights.

---

## 🔒 Privacy

- No login required  
- No internet connection needed  
- No cloud storage  
- All data stored locally on device  

---

## 🌱 Future Enhancements

- Daily scrolling limits  
- Personalized warning messages  
- Weekly reports  
- Cloud backup  
- iOS version  

---

## 👤 Author

**Yash Katiyar**

---

## 📌 Status

Currently in development 🚧  
Fighting the infinite scroll one build at a time.
