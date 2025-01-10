#!/bin/bash

cd /home/ec2-user/app

ln -s /etc/mymenu-backend.env .env

docker-compose build --no-cache