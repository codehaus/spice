#!/usr/bin/perl -w
#
# Script to generate and deploy components' web sites
# Author: Mauro Talevi

@JCOMPONENTS = ( );
@JCOMPONENTS_SANDBOX = ( "swingactions" );
@SPICE = ( "classman", "cli", "configkit", "converter", "jndikit", "loggerstore", "netserve", "salt", "threadpool", "xmlpolicy" );
@SPICE_SANDBOX = ( "metaclass", "metaschema", "nativekit", "xinvoke", "xsyslog" );

$COMMAND = "maven site:generate site:deploy";

if ( scalar @ARGV == 1 && $ARGV[0] eq "usage" ){
	&usage();
}

if ( scalar @ARGV > 2 ){
	&usage();
}

$componentType = "all";
$componentType = $ARGV[0] if ( $ARGV[0] );
$includeSandbox = "true" if ( $ARGV[1] );

@components = &getComponents();

my $currentDir = &getCurrentDir();

foreach ( @components ){
	my $cmd = "cd $currentDir/$_; $COMMAND";
	print "Executing $cmd\n";
	##exec($cmd);
}

exit;

sub getComponents{
    my @a = ( "site" ); # site is first component
    if ( $componentType ne "spice" ) {
    	push @a, grep(s/$_/components\/$_/,@JCOMPONENTS);
   		push @a, grep(s/$_/sandbox\/$_/,@JCOMPONENTS_SANDBOX) if ( $includeSandbox );
   	}
    if ( $componentType ne "jcomponent" ) {
    	push @a, grep(s/$_/components\/$_/,@SPICE);
    	push @a, grep(s/$_/sandbox\/$_/,@SPICE_SANDBOX) if ( $includeSandbox );
    }
    return @a;
}

sub getCurrentDir {
	my $pwd = `pwd`;
	chomp($pwd);
	return $pwd;
}

sub usage {
	print "usage: build-sites.pl [all|jcomponent|spice] [sandbox]";
	exit;
}
