# AuraStay — Bespoke Suites & Serene Sanctuary

AuraStay is a luxurious, modern Hotel Management System (HMS) application built with **Kotlin** and **Jetpack Compose** following **Material 3 Design Guidelines**. Engineered with offline-first local state persistence through a **Room Database** and powered by an **AI Concierge**, AuraStay provides a beautiful, high-fidelity experience for both premium guests and operations staff.

---

## 🎨 Visual Identity & Theme
AuraStay features a meticulously crafted aesthetic named **Velvet Obsidian**:
- **Canvas Base**: `#100F14` (Velvet Obsidian Black) paired with Deep Charcoal Slate (`#1A1822`).
- **Accents**: `#D4AF37` (Aesthetic GoldPrimary) for borders, highlights, and primary interactive elements.
- **Dynamic Elements**: High-contrast status indicators utilizing smooth gradients and modern rounded-corner elevation cards with customizable brush highlights.

---

## ✨ Features

### 🚪 1. Selected Reception & Check-In Portal
- **Selective Entrances**: Dynamically routes between the **Luxury Guest Entrance** and the **Staff Operations Desk**.
- **Credentials Panel**: Intuitive, validated credentials checkout (secured with staff override key `staff`).
- **Feature highlights**: Elegant interactive icons previewing the 5-Star Butler, Infinity Pool, Michelin Food, and Ritual Spa.

### 🛋️ 2. Luxury Guest Lounge (Guest Dashboard)
- **Bespoke Chamber Explorer**: Interactive carousel cards displaying suite attributes (duplex layouts, pricing rates, star ratings, and customizable high-contrast imagery).
- **Frictionless Booking & Billing**: Automated checkout scheduler with live night-count logic and dynamic ledger calculators.
- **In-Suite Dining & Orders**: In-room service ticket generator matching Guest stays directly with room parameters.

### 📊 3. Staff Operations Desk
- **Dynamic Housekeeping Controls**: One-tap status updates for room conditions, syncing instantly:
  - 🟢 **Available**
  - 🔴 **Occupied**
  - 🟡 **Cleaning**
  - 🔵 **Maintenance**
- **Live Canvas Analytics Graph**: Highly performant, custom-rendered drawing graph utilizing standard compose canvas vectors to represent operational yields, chamber demand leads, and weekly revenue curves.
- **Stay Logs & Checkout Actions**: High-visibility bookings manager allows staff to confirm check-ins or process immediate checkouts.

### 🌟 4. Private AI Butler (Executive Concierge)
- **Direct VIP Chat**: Immediate communications bridge for personalized service requests, spa recommendations, and Michelin dining details.
- **Intelligent Suggestion Chips**: One-tap luxury inquiries for instant butler interactions.
- **Animated Typing Prompts**: Clean, responsive user/model chat bubbles with custom status monitors.

---

## 🏗️ Architecture & Stack
AuraStay adheres to Clean Architecture guidelines utilizing standard Android practices:
- **UI Framework**: Jetpack Compose (100% Declarative UI)
- **Design System**: Android Material Design 3
- **Local Database**: Room persistence engine ensuring secure local caches of room catalogs, checkouts, and requests history.
- **State Flow**: Reactive programming model with Kotlin `StateFlow` and MVVM components.
- **Image Loader**: Coil (highly optimized async image transitions)
- **API Engine**: Ktor/Retrofit matching Google AI Client SDK.

---

## 🚀 Quick Navigation & Getting Started

### Prerequisites
- **Android Studio** (Koala or newer recommended)
- **JDK 17** or newer
- **Gradle 8.5+**

### Local Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/YOUR_USERNAME/aurastay.git
   cd aurastay
   ```
2. Open the project in Android Studio.
3. Sync Project with Gradle Files.
4. Set up your **Gemini API Key** in your environment configuration to enable full conversational concierge answers, or review the offline mock fallbacks.

### Operations / Credentials Override
- **Staff Email**: `staff@aurastay.com`
- **Access Password**: `staff`
