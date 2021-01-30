<?php

use Illuminate\Database\Seeder;
use Illuminate\Database\Eloquent\Model;
use App\Boutique;
use Illuminate\Support\Str;
use Illuminate\Support\Facades\DB;
class BoutiquesTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        DB::table('boutiques')->insert([
            [
            'boutowner' => '1',
            'iconurl' => 'hhhhhh',
            'libelle' => 'ffff',
            'adresse' => 'mmmmm',
            'tel' => '52223',
            'mail' => 'ggggg@gmail.com',
            'description' => 'llllll',
            'longitude' => '523.2',
            'latitude' => '888.3663',
        ],[
                'boutowner' => '2',
                'iconurl' => 'hhhhhh',
                'libelle' => 'ffff',
                'adresse' => 'mmmmm',
                'tel' => '52223',
                'mail' => 'ggggg@gmail.com',
                'description' => 'llllll',
                'longitude' => '523.2',
                'latitude' => '888.3663',
            ],[
                'boutowner' => '3',
                'iconurl' => 'hhhhhh',
                'libelle' => 'ffff',
                'adresse' => 'mmmmm',
                'tel' => '52223',
                'mail' => 'ggggg@gmail.com',
                'description' => 'llllll',
                'longitude' => '523.2',
                'latitude' => '888.3663',
            ],[
                'boutowner' => '4',
                'iconurl' => 'hhhhhh',
                'libelle' => 'ffff',
                'adresse' => 'mmmmm',
                'tel' => '52223',
                'mail' => 'ggggg@gmail.com',
                'description' => 'llllll',
                'longitude' => '523.2',
                'latitude' => '888.3663',
            ],[
                'boutowner' => '5',
                'iconurl' => 'hhhhhh',
                'libelle' => 'ffff',
                'adresse' => 'mmmmm',
                'tel' => '52223',
                'mail' => 'ggggg@gmail.com',
                'description' => 'llllll',
                'longitude' => '523.2',
                'latitude' => '888.3663',
            ],[
                'boutowner' => '6',
                'iconurl' => 'hhhhhh',
                'libelle' => 'ffff',
                'adresse' => 'mmmmm',
                'tel' => '52223',
                'mail' => 'ggggg@gmail.com',
                'description' => 'llllll',
                'longitude' => '523.2',
                'latitude' => '888.3663',
            ]



        ]);

    }

}
