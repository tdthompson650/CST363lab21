function switchDatabase(dbName) {
  db = db.getSiblingDB(dbName);
  print("Switched to database: " + dbName);
}

function getNextSequenceValue(sequenceName) {
  var sequenceDocument = db.database_sequences.findAndModify({
    query: { _id: sequenceName },
    update: { $inc: { seq: 1 } },
    new: true,
    upsert: true
  });
  return sequenceDocument.seq;
}

function insertDrugs() {
  var Drug = [
    { _id: getNextSequenceValue('drug_sequence'), name: "Aspirin" },
    { _id: getNextSequenceValue('drug_sequence'), name: "Ibuprofen" },
    { _id: getNextSequenceValue('drug_sequence'), name: "Amoxicillin" },
    { _id: getNextSequenceValue('drug_sequence'), name: "Lisinopril" }
  ];

  db.drug.insertMany(Drug);
  print("Drugs inserted successfully.");
}

function insertPharmacies() {
  var Pharmacy = [
    {
      _id: getNextSequenceValue('pharmacy_sequence'),
      name: "Pharmacy One",
      address: "101 Main St",
      phone: "555123456",
      drugCosts: [
        { drugName: "Aspirin", cost: 5.00 },
        { drugName: "Ibuprofen", cost: 8.00 }
      ]
    },
    {
      _id: getNextSequenceValue('pharmacy_sequence'),
      name: "Pharmacy Two",
      address: "202 Market St",
      phone: "555234567",
      drugCosts: [
        { drugName: "Amoxicillin", cost: 15.00 },
        { drugName: "Lisinopril", cost: 20.00 }
      ]
    }
  ];

  db.pharmacy.insertMany(Pharmacy);
  print("Pharmacies and associated drug costs inserted successfully.");
}

switchDatabase("lab21");
insertDrugs();
insertPharmacies();