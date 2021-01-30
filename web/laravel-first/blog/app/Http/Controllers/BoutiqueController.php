<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Boutique;
class BoutiqueController extends Controller
{
	 public function __construct()
    {
        $this->middleware('auth');
    } 
	
    public function index()
    {
        return Boutique::all();
		
		 $listeBoutique=Boutique::all();
        return view('boutique.index')->with('boutiques',$listeBoutique);
    }
	/* public function create()
    {
        return view('boutique.create');
    }*/

    public function show(Boutique $boutique)
    {
        return $boutique;
    }

    public function create(Request $request)
    {
        return view('add-boutique');
    }


    public function store(Request $request)
    {
        $boutique = Boutique::create($request->all());

        return response()->json($boutique, 201);
		
		
    }

    public function update(Request $request, Boutique $boutique)
    {
        $boutique->update($request->all());

        return response()->json($boutique, 200);
    }

    public function delete(Boutique $boutique)
    {
        $boutique->delete();

        return response()->json(null, 204);
    }
}
