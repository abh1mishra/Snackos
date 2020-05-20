const Express=require("express")
const Router=Express.Router();
const User = require("../Models/usersModel")
const jwt=require("jsonwebtoken")
Router.post("/posts",authenticateToken,(req,res,next)=>{
User.addPost(req.body.post,req.user,(err,num)=>{res.send("post added")})
})
function authenticateToken(req,res,next){
    const authToken=req.headers["authorization"]
    const token =authToken&&authToken.split(" ")[1]
    if(token==null){
       return  res.send("Forbidden")
    }
    else{
        jwt.verify(token,process.env.ACCESS_TOKEN_SECRET,(err,user)=>{
            if(err){return res.send("error")}
            req.user=user
            next()
        })
    }
}

module.exports=Router;