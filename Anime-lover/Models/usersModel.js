const mongoose=require("mongoose")
const bcrypt =require("bcrypt")
const saltRounds=10;
const userSchema=mongoose.Schema({
    name:{
    type:String,
    require:true
    },
    username:{
        type:String,
        require:true
    },
    posts:{
    type:Array,
    require:false,
    default:[]
    },
    email:{
        type:String,
        require:true
    },
    password:{
        type:String,
        require:true
    },
    dob:{
        type:String,
        require:false,
    },
    followers_id:{
        type:Array,
        require:false,
        default:[]
    },
    following_id:{
        type:Array,
        require:false,
        default:[]
    }
});
const userModel=module.exports = mongoose.model("users",userSchema,"users");

//model_functions


module.exports.getUserById=function(id,callback){
    userModel.findById(id,callback)
}

module.exports.getUserByUserName=function(uname,callback){
    const query = {username:uname}
    return userModel.findOne(query,callback)
}

module.exports.getUserByEmail=function(email,callback){
    const query = {email:email}
    return userModel.findOne(query,callback)
}
module.exports.passwordCheck=function(password,hash,callback){
    bcrypt.compare(password,hash,callback)
}

module.exports.addUser=function(user,callback){
    bcrypt.hash(user.password,saltRounds, function(err, hash) {
        // Store hash in your password DB.
        if(err){
            callback(err);
        }
        else{user.password=hash;
            user.save(callback);}
    });
}

module.exports.addPost=function(post,id,callback){
    userModel.updateOne({_id:id},{$push:{posts:post}},callback)
}