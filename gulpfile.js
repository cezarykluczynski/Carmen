var gulp = require( "gulp" );
var stylus = require( "gulp-stylus" );

gulp.task( "default", [ "styl" ] );

gulp.task( "styl", function () {
    gulp.src( "./src/main/webapp/resources/stylus/**/*.styl" )
        .pipe(stylus())
        .pipe( gulp.dest( "./src/main/webapp/resources/css" ) );
});