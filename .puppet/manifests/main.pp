Exec { path => [ "/bin/", "/sbin/" , "/usr/bin/", "/usr/sbin/" ] }

include java

class { 'ant':
  version => '1.9.1'
}

class { 'android':
  version => '22.0.1',
  user    => 'vagrant',
  group   => 'vagrant'
}

android::platform { 'android-17': }

exec { 'android path':
  command => "echo 'export PATH=${android::params::installdir}/android-sdk-linux/tools:${android::params::installdir}/android-sdk-linux/platform-tools:\$PATH' >> /home/vagrant/.bash_aliases",
  user    => 'vagrant',
  unless  => "which android >/dev/null 2>&1",
  require => Class['android']
}

include apt

apt::ppa { 'ppa:chris-lea/node.js': }
->
package { 'nodejs':
  ensure => latest
}

package { 'cordova':
  ensure => installed,
  provider => npm
}

package { ['curl', 'unzip', 'maven']:
  ensure => latest
}