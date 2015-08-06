var gulp = require( "gulp" );
var stylus = require( "gulp-stylus" );
var batch = require( "gulp-batch" );
var watch = require( "gulp-watch" );

gulp.task( "default", [ "styl" ] );

gulp.task( "styl", function () {
    gulp.src( "./src/main/webapp/resources/stylus/**/*.styl" )
        .pipe(stylus())
        .pipe( gulp.dest( "./src/main/webapp/resources/css" ) );
});

gulp.task("watch", function () {
    return watch( "./src/main/webapp/resources/stylus/**/*.styl", batch( function ( events, done ) {
        gulp.start( "styl", done ).pipe( "watch" );
    }));
});
