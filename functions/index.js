const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.sendMedicationReminder = functions.https.onRequest((req, res) => {
  const registrationToken = req.body.token;
  const medicationName = req.body.medicationName ||"Your medication";

  const message = {
    data: {
      title: "Medication Reminder",
      body: `It's time to take your ${medicationName} medication!`,
    },
    token: registrationToken,
  };

  admin.messaging().send(message)
      .then((response) => {
        console.log("Successfully sent message:", response);
        res.status(200).send("Notification sent successfully");
      })
      .catch((error) => {
        console.error("Error sending message:", error);
        res.status(500).send("Error sending notification");
      });
});
