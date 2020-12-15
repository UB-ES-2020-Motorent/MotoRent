// server.js
var express = require('express')
var path = require('path')
var serveStatic = require('serve-static')
app = express()
app.use(serveStatic(__dirname + "/dist"))
if (process.env.NODE_ENV === "production") {
  app.use(express.static("build"))
  app.get("*", (req, res) => {
    res.sendFile(path.resolve(__dirname,  "build", "index.html"))
  })
}
var port = process.env.PORT || 5000
app.listen(port)
console.log('server started '+ port)