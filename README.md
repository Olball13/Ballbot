# Android+Esp32 Ballbot
*A ballbot that uses the internal IMU and processing speed of an Android phone paired with an ESP32 through a serial USB-OTG connection*

<img alt="Picture of Ballbot" src="images/BallbotPortrait.png" width="400"/>

## Overview
---
A ballbot is an omnidirectional robot that balances and moves on a ball. It constantly measures its tilt, determines the magnitude of correction, and distributes the correct powers typically amongst 3 motors. Since I was introduced to this line of mobile robotics, I've always been fascinated to how the many subsystems of this type of robot work together.

This ballbot balances from 3 encoded motors on a standard size 7 basketball. Through the USB-OTG protocol, an Android phone communicates between an ESP32 to compute a PWM signal to each motor. Intern, encoder detections are accumulated to center the bot and maintain precise movements along a direction.

I am relatively new to coding and mechatronics as a whole, but I have found my passion. The goal of starting this project is to gain experience in Java coding, and learn the components of app development in the Android Studio framework. I also strive to dip my toes in C++ and the Arduino IDE, some Javascript and HTML, and basic Electronics with an introduction to soldering.

Though this project is for my education, I plan to make this open-source, instructing and documenting my whole process.



## Hardware Stack
---
*Soldering needed | 3D-Printer needed*
* Android Pocket Device with USB port, Camera, Gyroscope and Accelerometer
* [ESP32](https://www.amazon.ca/ESP-WROOM-32-NodeMCU-Bluetooth-Development-Microcontroller/dp/B0CHBMFJBQ?source=ps-sl-shoppingads-lpcontext&ref_=fplfs&th=1) with Pulse Counter and OTG Protocol
* ***2x*** [TB6612FNG Duel Motor Drivers](https://www.amazon.ca/-/fr/Podazz-modules-pilote-TB6612FNG-performance/dp/B0DB1DT78W?source=ps-sl-shoppingads-lpcontext&ref_=fplfs&psc=1&smid=A38RLFO44KZ1BT) (or other motor drivers to support 3 motors with PWM inputs)
* ***3x*** [JGB37‑520 530RPM Motors](https://www.amazon.ca/flexman-JGB37%E2%80%91520-Reduction-Supporting-Appliances/dp/B0CRDSD9P8?source=ps-sl-shoppingads-lpcontext&ref_=fplfs&th=1), chosen for their ideal speed, torque, encoder features, and cost
* [TalentCell Rechargeable Battery Pack](https://www.amazon.ca/TalentCell-Rechargeable-12000mAh-Multi-led-indicator/dp/B00ME3ZH7C?source=ps-sl-shoppingads-lpcontext&ref_=fplfs&psc=1&smid=A24OIV2F0TL1K3), easy and safe charging, having a reliable 12V supply to all 3 motors
* A 3D-printed chassis + Omni Wheels -> [**Onshape Design**](https://cad.onshape.com/documents/378b20639c0a0bd1a3ce8146/w/8ba2296efa7b630b66d342ee/e/65fec9f27fb9d7718af92dce)


### Other house-keeping items:
* Size 7 Basketball
* USB-OTG Cable
* DC Power Jack Plug Adapter Barrel Connector
* 18 & 20AWG Solid Core Wires
* Breadboard (Optional)
* M3 Hex Screws and Bolts + Finishing nails


### Custom 3D-Printed Omni Wheels

<img alt="Picture of a 3D-Printed Omni Wheel" src="images/OmniWheel.png" width="400"/>

In the search to find a cheap alternative to expensive omni wheels on the market, I decided to print my own! Though I have not tested the quality/functionality of the design yet, I plan to perfect the model once I have access to my school's 3D-printer next September. Standard PLA is strong enough for the design but some TPU with 95+ hardness would be ideal to print the rollers. I plan to use modified finishing nails as axles and M3 bolts & screws to Couple the wheels with the motors.


## Software Stack
---
***Android App***
* The computational brain of the ballbot, programed in Java. Contains the imu, PD+F loop, and inverse kinematics to calculate each motor PWM value.

***ESP32 Firmware***
* *As of this prototype*, containing a basic script that passes the PWM signals from the phone to the respective motors.

***Onshape CAD Platform***
* The online Cad software I use to model the chassis and omni wheels of the bot. Though it has shown to be restrictive at times, I find it easy to pick up and satisfactory for my parametric design.

### PD+F Controller
The type of controller used to keep the ballbot balanced is a Proportional Derivative Feedforward controller. A PD controller works well since it negates the error with dampening to the setpoint. Introducing an feedforward term, based off of the affects of gravity, accounts for the context that we have for a ballbot system. A top-heavy ballbot acts such as an inverted pendulum, the primary force being gravity. Based on the tilt of the bot, the raising forces of gravity can be calculated. The more the angle error, the bigger the lever arm, the greater gravity acts, making the bot fall faster. It's interesting to note, the feedforward term is calculated using a sine wave to emulate the accumulating gravity force:

> ### Ballbot Gravity Feedforward
>
> ```math
> Target = m * g * l * sin(θ)
> ```
> Simplified to:
> ```math
> Target = Kf * sin(θ)
> ```
>
> **Where:**
>
> * `Target` : Target feedforward motor power
> * `m` : Total mass of the bot above the ball
> * `g` : Acceleration of  gravity
> * `l` : Distance from the center of the ball to the CoM of the upper body (Lever arm length)
> * `θ` : Current 1D tilt magnitude from vertical position
> * `Kf` : Simplified gravity compensation gain (``m * g * l``)

The whole PD+F equation finds the general 1D magnitude of compensation. What I mean by this is that the equation only looks at the tilt from one vector. Before this equation, the heading of where the bot is tilting and the magnitude of tilt is found from averaging the pitch and roll readings. The controller then calculates the compensation from that 1D perspective. This is better then the messiness of having two PID controllers responding to both Pitch and Roll errors and trying to combine them together.



## Roadmap Checklist
---
### Prototype
*To start this project, I will create a "proof of concept", to learn the basics and to later build off a simple framework. I will use a failed print from V1 of my Chassis model, and just focus on balancing without complex encoder kinematics to center the bot's position.*
#### Coding
* [x] Access IMU Sensors on Phone
* [] PD+F Logic
* [] Use Inverse Kinematics to convert PD+F output to motor power ratios
* [] Establish efficient OTG communication

#### Building/Electronics
* [] Solder all Components
* [] Ensure working Omni Wheel Design
* [] Test PWM signals with motors

#### Putting it Together
* [] Convert Motor ratios to PWM signals
* [] Refine, Tune, and Test final product



## My Vision for this Project
---
Depending on the efficiency of the bot, I plan to test the ESP32 to host a local website where I may tune control constants from my computer and potentially control basic movement of the bot with a usb gamepad.

This would be accomplished through the **Gamepad API feature** that all websites natively have in Javascript.

I also may use both cameras of the phone, experimenting with OpenCV, to follow dynamic paths, objects andor people. I may be pushing the bandwidth of this project, but I can also send a low priority video stream to the local website through the wifi-network. This process is broadly known as **HTTP MJPEG streaming**.

For now, this is all just talk, but I will try to make this a reality. Sadly the responsibilities of school burden my schedule with this project.


### Endless Possibilities with Ballbots

In similar projects, I can see myself experimenting with Ballbots of a near [**Neutral Equilibrium**](https://www.youtube.com/watch?v=4rG9u478X1Q). A Neutral Equilibrium means that the bot's CoG is at the exact center of its ball. This introduces a unique response of neither acting as a pendulum or inverted pendulum.

To put it simply, at a high CoG, the bot must constantly attempt to center itself from the opposing lever of gravity. This costs power, burdens stability, but demonstrates high speed and agility. when the CoG is low, gravity is on your side so the bot stays centered like a sumo toy. However, this impacts speed greatly. [**Video illustrating Concept**](https://www.youtube.com/watch?v=Oo9H970JLg8)

Areas to explore are the applications of [**Slosh Dynamics**](https://www.youtube.com/watch?v=h9qBrYYJFlU&t=79s), liquids in a ballbot which naturally conform to gravity, dynamically changing the CoG as a physical dampening mechanism. Imagine a ballbot with around a neutral equilibrium, but with ballasts of viscous liquid. It would carry proprieties of a low push from gravity, dampening and possibly modest inverted pendulum movement with the liquid, relevant speed, all with minimal effort from the motors.

Other than blatant manipulations of the CoG, a **Propeller or Reaction Wheel design** may prove beneficial on many levels. Rather than relying on friction to the ball, these systems would use their internal momentum or aerodynamic thrust. This can drastically increase the mechanical advantage, as instead of actuating from the stem of the ballbot, they would push from a higher perspective, a longer lever arm!

*Then another problem emerges...* the base of the bot is not secured to the ball. With a longer lever arm, the force pushes between the bot and the ball like pushing the top of a Jenga tower ***-This also applies to Liquid Placement***. Now its not that dramatic of a difference and this can be overlooked with a heavy structure. **Here is a concept of a ballbot that would fix this this problem:**

*The 3 omni wheels of the bot would position vertically, resting between the northern and southern hemispheres of the ball. They would tilt 45deg along the x-axis to support rotational manipulation. Then a structure would hold the wheels in place with other passive omni wheels, both hugging the top and bottom of the ball. This attaches such as a spring-loaded ball point pen. The powered omni wheels would never loose contact of the ball, even if the bot were picked up. Since the bot is secured to the ball, this Jenga Tower phenomenon would not take place, only supplying minimal stress to this new base.*

There are truly endless possibilities with ballbots. I haven't even touched on projects such as [**RoboBall**](https://www.youtube.com/watch?v=iiMYEe-CIrY), though they are not technically ballbots. I can't wait to explore hybrid designs of ballbots and other things in robotics.



## Helpful Resources
---
*Some interesting resources that have helped me in this project...*

### How Android Apps Work
* [The Activity Lifecycle](https://developer.android.com/guide/components/activities/activity-lifecycle)
* [(YOUTUBE) How to Build Your First Android App in Java](https://www.youtube.com/watch?v=Wd9TN4fGutM)

### Motion Sensors
* [Which sensors are required for finding rotation vector](https://stackoverflow.com/questions/30576313/sensor-fusion-which-sensors-are-required-for-finding-rotation-vector)
* [Android Motion Sensors](https://developer.android.com/develop/sensors-and-location/sensors/sensors_motion#java)

### PD+F Controller
* [(YOUTUBE) Vectors](https://www.youtube.com/watch?v=iXdMpXMuEGI)
* [Implementing a PID Controller in Java](https://4comprehension.com/java-pid-controller/)
* [Feedforward Control](https://www.ctrlaltftc.com/feedforward-control)

### Phone+ESP32
* [(YOUTUBE) ESP32, ESP8266 and Android Communication over USB](https://www.youtube.com/watch?v=CfonnuwjqE4)
* [(YOUTUBE) ESP32, ESP8266 and Android - Communication over USB, basic code upgraded](https://www.youtube.com/watch?v=LVJ0YeTKmtc)