# 🎧 Reels & Shorts Counter

An Android digital wellbeing app that tracks how many Instagram Reels and YouTube Shorts you scroll — and how much time you spend doing it.  
Because sometimes the biggest lie we tell ourselves is “just one more reel”.

---

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
