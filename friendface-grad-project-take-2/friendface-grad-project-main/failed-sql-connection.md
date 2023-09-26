## Debugging a failed connection to MySQL/MariaDB

If connection fails: we need to revisit our assumptions. Here's some sanity checks that you can do.

You can check whether there's a process `mysqld.exe` bound to port 3306, and see which IP addresses it's bound to:

```bash
netstat -ab
```

You can check whether it's possible to talk to the service running on `localhost:3306`:

```bash
curl -o - telnet://localhost:3306
```

You can check Task Manager (press Ctrl Shift Esc) to see whether there's a MySQL or MariaDB process running.

If you are connected, but your password is rejected: try a blank password (this is the default for the root user in MariaDB).