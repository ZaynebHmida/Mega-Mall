<?php

use Illuminate\Database\Seeder;
use App\User;
use Illuminate\Support\Facades\DB;

class UsersTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        DB::table('users')->insert([
            [
                'email' => 'ggggg@gmail.com',
                'password' => 'zayneb123',
                'name' => 'zayneb',
              

            ],[
                'email' => 'hhhhhh@gmail.com',
                'password' => 'hhhh123',
                'name' => 'hhhhhh',

            ],[
                'email' => 'llllll@gmail.com',
                'password' => 'llll123',
                'name' => 'lllll',

            ],[
                'email' => 'mmmmmm@gmail.com',
                'password' => 'mmmmm123',
                'name' => 'mmmmmm',

            ],[
                'email' => 'vvvvvv@gmail.com',
                'password' => 'vvvvv123',
                'name' => 'vvvvvv',

            ]
    ]);
    }
}
