var gulp=require('gulp');
var pug = require('gulp-pug');
gulp.task('default',function buildHtml(){
    return gulp.src('*.pug')
        .pipe(pug({
            // Your options in here. 
            pretty:true,
            compileDebug:true
        }))
        .pipe(gulp.dest('web'))
});