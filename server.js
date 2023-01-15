var express = require("express");
var app = express();
var sql = require("mysql");
var bodyParser = require("body-parser");
var crypto = require("crypto");

var conn = sql.createConnection({
  host: "localhost",
  user: "admin",
  password: "admin",
});

var db = sql.createConnection({
  host: "localhost",
  user: "admin",
  password: "admin",
  database: "sae",
});

app.use(bodyParser.urlencoded({ extended: true }));

app.use("/public", express.static("public"));

app.set("view engine", "ejs");


app.get("/login", function (req, res) {
  res.render("pages/login", {
    msg: "",
  });
});


app.post("/login", (req, res) => {
  console.log(req)
  var data;
  // création d'un hash pour le mot de passe ;
  let hash = crypto.createHash("md5").update(req.body.password).digest("hex");
  db.query(`SELECT * FROM USERS WHERE login = '${req.body.id}' AND password = '${hash}'`,(err, rep) => {
      if (err) throw err;
      data = JSON.parse(JSON.stringify(rep));
      if (data.length == 0) {
        res.render("pages/login", {
          msg: "Login ou mot de passe incorect",
        });
        
      } else {
        res.redirect("/");
      }
    }
  );
});

// index page
app.get("/", function (req, res) {  
  db.query(`SELECT * from partitions ORDER BY i ASC`, (err, res, fields) => {
    if (err) throw err;
    data = JSON.parse(JSON.stringify(res));
  });
  var tagline = "Ensemble des Partitions : ";
  var reverseData = data.reverse();
  var lastData = [];
  for (var i = 0; i < reverseData.length; i++) lastData.push(reverseData[i]);
  for(let i = 0 ; i < data.length ; i++)data[i].date = new Date(data[i].date);
  if (data === []) {
    res.render("pages/index"),{
        data: "Il n'y a aucunes partiton enregistrées",
        tagline: tagline,
      };
  } else {
    res.render("pages/index", {
      tagline: tagline,
      last : lastData,
      data: data.reverse(),
    });
  }
});

app.get("/register", (req, res) => {
  res.render("pages/register",{
    msg : ""
  });
});

app.post("/register", (req, res) => {
  var data;
  // création d'un hash pour le mot de passe ;
  if (req.body.password === "" || req.body.password === "") {
    res.render("pages/register", { msg: "veuillez saisir un mdp" });
  } else {
    let hash = crypto.createHash("md5").update(req.body.password).digest("hex");
    db.query(
      `SELECT * FROM USERS WHERE login = '${req.body.id}'`,
      (err, rep) => {
        if (err) throw err;
        data = JSON.parse(JSON.stringify(rep));
        if (data.length == 0) {
          db.query(
            `INSERT INTO USERS(login, password) VALUES ('${req.body.id}','${hash}' )`,
            (err, result) => {
              if (err) throw err;
              res.render("pages/login", {
                msg: "Utilisateur ajouté",
              });
            }
          );
        } else {
          res.render("pages/register", {
            msg: "Nom d'utilisateur deja utilisé",
          });
        }
      }
    );
  }
});

app.get("/error", (req, res) => {res.render("pages/error");});

app.all("*", function (req, res) {throw new Error("Bad request");});

app.use(function (e, req, res, next) {
  if (e.message === "Bad request") {
    res.render("pages/error");
  }
});

var port = 8080;
app.listen(port);

console.log("URL: http://localhost:" + port + "/login");