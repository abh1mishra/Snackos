const mongoose =require("mongoose");
module.exports.dbConnect=function(callback){
mongoose.connect('mongodb+srv://sasadick456:'+process.env.password+'@cluster0-qh7hv.mongodb.net/test?retryWrites=true&w=majority', { useNewUrlParser: true,useUnifiedTopology: true ,dbName:"anime"})
  mongoose.connection.once('open', res => {
    callback();
  });
}