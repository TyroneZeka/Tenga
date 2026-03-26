import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { useNavigate, Link } from 'react-router-dom'
import { ArrowLeft } from 'lucide-react'
import { useCreateListing } from '@/features/listings'
import { uploadListingImages } from '@/features/listings/api/listingsApi'
import { CURRENCIES, CONDITIONS } from '@/lib/constants'
import { CATEGORIES, ZIMBABWE_CITIES } from '@/lib/filterOptions'

const schema = z.object({
  title: z.string().min(5, 'Title must be at least 5 characters').max(150),
  description: z.string().min(10, 'Description must be at least 10 characters').max(5000),
  price: z.coerce.number().min(0.01, 'Price must be greater than 0'),
  currency: z.enum(['ZIG', 'USD']),
  condition: z.enum(['NEW', 'LIKE_NEW', 'GOOD', 'FAIR', 'POOR']),
  categoryId: z.string().min(1, 'Category is required'),
  city: z.string().min(1, 'City is required'),
  negotiable: z.boolean(),
})

type FormValues = z.infer<typeof schema>

export default function CreateListingPage() {
  const navigate = useNavigate()
  const { mutateAsync: createListing, isPending, error } = useCreateListing()

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<FormValues>({
    resolver: zodResolver(schema),
    defaultValues: { currency: 'USD', condition: 'GOOD', negotiable: false },
  })

  async function onSubmit(data: FormValues) {
    const listing = await createListing(data)
    const fileInput = document.getElementById('images') as HTMLInputElement
    if (fileInput?.files?.length) {
      await uploadListingImages(listing.id, Array.from(fileInput.files))
    }
    navigate(`/listings/${listing.id}`)
  }

  return (
    <div className="min-h-screen bg-gray-50 pb-20">
      <div className="mx-auto max-w-2xl px-4 py-6">
        <Link to="/" className="mb-4 flex items-center gap-1 text-sm text-gray-500 hover:text-gray-800">
          <ArrowLeft className="h-4 w-4" /> Back
        </Link>
        <h1 className="mb-5 text-xl font-semibold text-gray-900">Create listing</h1>

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4 rounded-xl bg-white p-5 shadow-sm">

          {/* Server error */}
          {error && (
            <div className="rounded-lg bg-red-50 px-4 py-3 text-sm text-red-700">
              Failed to create listing. Please check your details and try again.
            </div>
          )}

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">Title</label>
            <input
              {...register('title')}
              placeholder="What are you selling?"
              className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-primary-500 focus:outline-none focus:ring-1 focus:ring-primary-500"
            />
            {errors.title && <p className="mt-1 text-xs text-red-600">{errors.title.message}</p>}
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">Description</label>
            <textarea
              {...register('description')}
              rows={4}
              placeholder="Describe your item — condition, history, reason for selling…"
              className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-primary-500 focus:outline-none focus:ring-1 focus:ring-primary-500"
            />
            {errors.description && (
              <p className="mt-1 text-xs text-red-600">{errors.description.message}</p>
            )}
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="mb-1 block text-sm font-medium text-gray-700">Price</label>
              <input
                {...register('price')}
                type="number"
                min={0}
                step="0.01"
                placeholder="0.00"
                className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-primary-500 focus:outline-none focus:ring-1 focus:ring-primary-500"
              />
              {errors.price && <p className="mt-1 text-xs text-red-600">{errors.price.message}</p>}
            </div>
            <div>
              <label className="mb-1 block text-sm font-medium text-gray-700">Currency</label>
              <select
                {...register('currency')}
                className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-primary-500 focus:outline-none focus:ring-1 focus:ring-primary-500"
              >
                {CURRENCIES.map((c) => (
                  <option key={c} value={c}>
                    {c === 'USD' ? 'US Dollar (USD)' : 'Zimbabwe Gold (ZiG)'}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="mb-1 block text-sm font-medium text-gray-700">Category</label>
              <select
                {...register('categoryId')}
                className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-primary-500 focus:outline-none focus:ring-1 focus:ring-primary-500"
              >
                <option value="">Select category…</option>
                {CATEGORIES.map((cat) => (
                  <option key={cat.id} value={cat.id}>
                    {cat.name}
                  </option>
                ))}
              </select>
              {errors.categoryId && (
                <p className="mt-1 text-xs text-red-600">{errors.categoryId.message}</p>
              )}
            </div>
            <div>
              <label className="mb-1 block text-sm font-medium text-gray-700">Condition</label>
              <select
                {...register('condition')}
                className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-primary-500 focus:outline-none focus:ring-1 focus:ring-primary-500"
              >
                {CONDITIONS.map((c) => (
                  <option key={c} value={c}>
                    {c.replace('_', ' ')}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">City</label>
            <select
              {...register('city')}
              className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-primary-500 focus:outline-none focus:ring-1 focus:ring-primary-500"
            >
              <option value="">Select city…</option>
              {ZIMBABWE_CITIES.map((city) => (
                <option key={city} value={city}>
                  {city}
                </option>
              ))}
            </select>
            {errors.city && <p className="mt-1 text-xs text-red-600">{errors.city.message}</p>}
          </div>

          <div className="flex items-center gap-2">
            <input
              {...register('negotiable')}
              id="negotiable"
              type="checkbox"
              className="h-4 w-4 rounded border-gray-300 text-primary-500 focus:ring-primary-500"
            />
            <label htmlFor="negotiable" className="text-sm text-gray-700">
              Price is negotiable
            </label>
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Photos <span className="text-gray-400">(optional)</span>
            </label>
            <input
              id="images"
              type="file"
              accept="image/*"
              multiple
              className="w-full text-sm text-gray-600 file:mr-3 file:rounded-lg file:border-0 file:bg-primary-50 file:px-3 file:py-1.5 file:text-sm file:text-primary-700 hover:file:bg-primary-100"
            />
          </div>

          <button
            type="submit"
            disabled={isPending}
            className="w-full rounded-lg bg-primary-500 py-2.5 text-sm font-medium text-white hover:bg-primary-600 disabled:opacity-50"
          >
            {isPending ? 'Creating…' : 'Create listing'}
          </button>
        </form>
      </div>
    </div>
  )
}
