const LocalStrategy = require("passport-local").Strategy
const User=require("../Models/usersModel")
module.exports.strategy = new LocalStrategy({usernameField:"email",passwordField:"password"},
        function(username, password, done) {
            console.log("passport chutiyaap")
            if(username.includes("@")){
                User.getUserByEmail(username,passCheck);
            }
            else{
                User.getUserByUserName(username,passCheck)
            }
            function passCheck(err, user) {
                if (err) { return done(err); }
                if (!user) {
                  return done(null, false, { message: 'Incorrect email.' });
                }
                else{
                    User.passwordCheck(password,user.password,function(err,result){
                        if(err){return done(err)}
                        if(!result){
                            return done(null,false,{message:"password error"})
                        }
                        else{
                            return done(null,{message:"Authenticated"})
                        }
                    })
                }
                return done(null, user);
              }
        }
      );

