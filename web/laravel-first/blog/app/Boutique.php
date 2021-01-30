<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Notifications\Notifiable;
use Illuminate\Contracts\Auth\MustVerifyEmail;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Contracts\Auth\CanResetPassword;

class Boutique extends Model
{
    protected $fillable = ['boutowner', 'iconurl', 'libelle', 'adresse', 'mail', 'tel', 'description', 'longitude', 'latitude'];
}
