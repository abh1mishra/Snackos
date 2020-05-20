const Express= require("express")
const User=require("../Models/usersModel")
const Router=Express.Router()
const jwt=require("jsonwebtoken")
const passport = require("passport")
const {check,validationResult} = require("express-validator")
/* Below in array after /register are the validation arrays , so add required sanitizaton methods using custom validators */
Router.post("/register",[
check("uname").trim().escape().isLength({min:5}).withMessage("Short Username").custom(value => {
    return User.getUserByUserName(value).then(user => {
    if (user) {
        return Promise.reject('username already in use');
    }
    });
}),
check('confirmPassword').custom((value, { req }) => {
    if (value !== req.body.password) {
      throw new Error('Password confirmation does not match password');
    }
    
    // Indicates the success of this synchronous custom validator
    return true;
  }),

/*enter password validation and sanitization logic*/

// check("password").matches(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/, "i").withMessage("password not valid"),

/*check if email validation is correct */

check("email").isEmail().withMessage("Not a valid email").normalizeEmail().trim().escape().custom(value => {
    return User.getUserByEmail(value).then(user => {
      if (user) {
        return Promise.reject('E-mail already in use');
      }
    });
  })],(req,res)=>{
    const result=validationResult(req)
    if(result.isEmpty()){
        const newUser=new User({
            name:req.body.name,
            username:req.body.uname,
            email:req.body.email,
            password:req.body.password
        });
        User.addUser(newUser,(err)=>{
            if(err){
                // console.log(err.customMSg);
            res.json({success:false,msg:"Failed"});}
            else{
                res.json({success:true,msg:"Successful"})
            }
        }) 
    }
    else{
        res.json(result);
    }




})
Router.post("/login", passport.authenticate('local',{session:false,failureRedirect:"/login"}),function(req,res){
    const accessToken=jwt.sign(req.user.id,process.env.ACCESS_TOKEN_SECRET)
    res.json(accessToken)
})
module.exports=Router