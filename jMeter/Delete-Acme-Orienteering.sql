start transaction;

use `Acme-Orienteering`;

revoke all privileges on `Acme-Orienteering`.* from 'acme-user'@'%';

revoke all privileges on `Acme-Orienteering`.* from 'acme-manager'@'%';

drop user 'acme-user'@'%';
drop user 'acme-manager'@'%';

drop database `Acme-Orienteering`;

commit;